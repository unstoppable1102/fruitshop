package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.CategoryRequest;
import com.bkap.fruitshop.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAll();
    CategoryResponse findById(Long id);
    CategoryResponse save(CategoryRequest request);
    CategoryResponse update(Long id, CategoryRequest request);
    void delete(Long id);
    List<CategoryResponse> findByName(String name);
}
