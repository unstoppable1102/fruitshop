package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.common.util.UploadFileUtil;
import com.bkap.fruitshop.dto.request.ProductRequest;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.ProductResponse;
import com.bkap.fruitshop.entity.Category;
import com.bkap.fruitshop.entity.Product;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.CategoryRepository;
import com.bkap.fruitshop.repository.OrderItemRepository;
import com.bkap.fruitshop.repository.ProductRepository;
import com.bkap.fruitshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UploadFileUtil uploadFileUtil;
    private final OrderItemRepository orderItemRepository;

    @Override
    public PageResponse<ProductResponse> getAllProducts(String keyword, Double minPrice, Double maxPrice, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);
        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, cb) ->
                    cb.like(cb.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%"));
        }
        if (minPrice != null) {
            spec = spec.and(((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice)));
        }
        if (maxPrice != null) {
            spec = spec.and(((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice)));
        }
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();
        return new PageResponse<>(productPage.getNumber(), productPage.getSize(),
                productPage.getTotalElements(), productPage.getTotalPages(), productPage.isLast(), productResponses);
    }

    @Override
    public PageResponse<ProductResponse> getProductsByCategory(Long categoryId, String keyword, Double minPrice, Double maxPrice, Pageable pageable) {
        Specification<Product> spec = Specification.where((root, criteriaQuery, cb) ->
            cb.equal(root.get("category").get("id"), categoryId));

        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, cb) ->
                    cb.like(cb.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%"));
        }

        if (minPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice)
            );
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice)
            );
        }

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map((product) -> modelMapper.map(product, ProductResponse.class))
                .toList();

        return new PageResponse<>(
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isLast(),
                productResponses
        );

    }

    @Override
    public ProductResponse getProductById(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (productRepository.existsByProductName(request.getProductName())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }

        Product product = Product.builder()
                .productName(request.getProductName())
                .status(request.isStatus())
                .price(request.getPrice())
                .priceOld(request.getPriceOld())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .category(category)
                .image(uploadFileUtil.saveImage(request.getImage()))
                .build();

        return modelMapper.map(productRepository.save(product), ProductResponse.class);
    }

    @Override
    public ProductResponse updateProduct(long id, ProductRequest request) {

        //find category by id
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        //find product by id
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        String imagePath = (request.getImage() != null && !request.getImage().isEmpty())
                ? uploadFileUtil.saveImage(request.getImage())
                : existingProduct.getImage();

        Product updatedProduct = Product.builder()
                .id(existingProduct.getId())
                .productName(request.getProductName())
                .status(request.isStatus())
                .price(request.getPrice())
                .priceOld(request.getPriceOld())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .category(category)
                .image(imagePath)
                .build();

        return modelMapper.map(productRepository.save(updatedProduct), ProductResponse.class);
    }

    @Override
    public void deleteProduct(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            uploadFileUtil.deleteImage(product.getImage());
        }
        productRepository.delete(product);

    }

    @Override
    public List<ProductResponse> getRelatedProducts(Long id) {

        Product currentProduct = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        List <Product> relatedProducts = productRepository.findTop8ByCategoryIdAndIdNot(currentProduct.getCategory().getId(), id);
        return relatedProducts.stream()
                .map(this::toProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> get8LatestProducts() {
        List<Product> latestProducts = productRepository.findTop8ByOrderByCreatedAtDesc();
        return latestProducts.stream()
                .map(this::toProductResponse)
                .toList();
    }

    //TODO
    @Override
    @Transactional
    public void updateProductStatus(long id, boolean status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setStatus(status);
        productRepository.save(product);
    }

    //TODO
    @Override
    @Transactional
    public void updateProductQuantity(long id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        if (quantity <= 0){
            throw new AppException(ErrorCode.INVALID_QUANTITY);
        }
        int oldQuantity = product.getQuantity();
        product.setQuantity(oldQuantity + quantity);
        productRepository.save(product);
    }

    //TODO
    @Override
    public List<ProductResponse> getBestSellingProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> bestSellingData = orderItemRepository.findBestSellingProductIds(pageable);

        List<Long> productIds = bestSellingData.stream()
                .map(row -> (Long) row[0])
                .toList();

        List<Product> products = productRepository.findAllById(productIds);

        //Map sản phẩm theo id
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        return bestSellingData.stream()
                .map(row -> {
                    Long productId = (Long) row[0];
                    Product product = productMap.get(productId);
                    if (product == null) return null;

                    ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
                    productResponse.setSoldQuantity(((Long) row[1]).intValue());

                    return productResponse;
                })
                .filter(Objects::nonNull)
                .toList();

    }

    //TODO
    @Override
    public List<ProductResponse> getDiscountedProducts() {
        List<Product> discountedProducts = productRepository.findByPriceOldGreaterThanPrice();

        // Chuyển đổi sang ProductResponse sử dụng ModelMapper
        return discountedProducts.stream()
                .map(this::toProductResponse)
                .toList();
    }

    private ProductResponse toProductResponse(Product product) {

        ProductResponse response = modelMapper.map(product, ProductResponse.class);

        if (product.getCategory() != null) {
            response.setCategoryName(product.getCategory().getName());
        }
        return response;
    }
}
