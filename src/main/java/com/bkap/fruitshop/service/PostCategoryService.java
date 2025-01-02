package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.PostCategoryRequest;
import com.bkap.fruitshop.dto.response.PostCategoryResponse;

import java.util.List;

public interface PostCategoryService {
    List<PostCategoryResponse> findAll();
    List<PostCategoryResponse> findByName(String name);
    PostCategoryResponse findById(Long id);
    PostCategoryResponse create(PostCategoryRequest request);
    PostCategoryResponse update(long id, PostCategoryRequest request);
    void delete(long id);
}
