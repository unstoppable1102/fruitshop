package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.common.util.UploadFileUtil;
import com.bkap.fruitshop.dto.request.ProductRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final UploadFileUtil uploadFileUtil;

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map((element) -> modelMapper.map(element, ProductResponse.class))
                .collect(Collectors.toList());
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
        modelMapper.map(request, product);

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
        productRepository.deleteById(id);

    }

    @Override
    public List<ProductResponse> findByProductName(String name) {
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(name);
        return products.stream()
                .map((element) -> modelMapper.map(element, ProductResponse.class))
                .collect(Collectors.toList());
    }
}
