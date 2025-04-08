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
import com.bkap.fruitshop.repository.ProductRepository;
import com.bkap.fruitshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UploadFileUtil uploadFileUtil;

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
        Page<Product> productPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productRepository.findByCategoryIdAndProductNameContainingIgnoreCase(categoryId, keyword, pageable);
        }else if (minPrice != null && maxPrice != null) {
            productPage = productRepository.findByCategoryIdAndPriceBetween(categoryId, minPrice, maxPrice, pageable);
        }else {
            productPage = productRepository.findByCategoryId(categoryId, pageable);
        }

        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();
        return new PageResponse<>(productPage.getNumber(), productPage.getSize(),
                productPage.getTotalElements(), productPage.getTotalPages(), productPage.isLast(), productResponses);
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

        Product product = modelMapper.map(request, Product.class);
        product.setCategory(category);
        product.setId(null);

        //Xu ly lưu file ảnh
        String imagePath = uploadFileUtil.saveImage(request.getImage());
        product.setImage(imagePath);

        return modelMapper.map(productRepository.save(product), ProductResponse.class);
    }

    @Override
    public ProductResponse updateProduct(long id, ProductRequest request) {

        //find category by id
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        //find product by id
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setProductName(request.getProductName());
        product.setStatus(request.isStatus());
        product.setPrice(request.getPrice());
        product.setPriceOld(request.getPriceOld());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            //save image into folder
            String imagePath = uploadFileUtil.saveImage(request.getImage());
            product.setImage(imagePath);
        }

        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductResponse.class);
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

        List <Product> relatedProducts = productRepository.findByCategoryIdAndIdNot(currentProduct.getCategory().getId(), id);
        return relatedProducts.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();
    }

    @Override
    public List<ProductResponse> get8LatestProducts() {
        List<Product> latestProducts = productRepository.findTop8ByOrderByCreatedAtDesc();
        return latestProducts.stream()
                .map((element) -> modelMapper.map(element, ProductResponse.class))
                .toList();
    }

    //TODO
    @Override
    public void updateProductStatus(long id, String status) {

    }

    //TODO
    @Override
    public void updateProductQuantity(long id, int quantity) {

    }

    //TODO
    @Override
    public List<ProductResponse> getBestSellingProducts(int limit) {
        return List.of();
    }

    //TODO
    @Override
    public List<ProductResponse> getDiscountedProducts() {
        List<Product> discountedProducts = productRepository.findByPriceOldGreaterThanPrice();
        // Chuyển đổi sang ProductResponse sử dụng ModelMapper
        return discountedProducts.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .toList();
    }
}
