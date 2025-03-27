package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.ProductRequest;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(long id);
    PageResponse<ProductResponse> getAllProducts(String keyword, Double minPrice, Double maxPrice, Pageable pageable);
    PageResponse<ProductResponse> getProductsByCategory(Long categoryId, String keyword, Double minPrice, Double maxPrice, Pageable pageable);
    ProductResponse updateProduct(long id, ProductRequest request);
    void deleteProduct(long id);
}
