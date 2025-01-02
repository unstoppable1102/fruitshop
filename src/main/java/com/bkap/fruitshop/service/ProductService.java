package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.ProductRequest;
import com.bkap.fruitshop.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(long id);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(long id, ProductRequest request);
    void deleteProduct(long id);
    List<ProductResponse> findByProductName(String name);
}
