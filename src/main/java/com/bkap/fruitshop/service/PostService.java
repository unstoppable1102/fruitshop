package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.PostRequest;
import com.bkap.fruitshop.dto.response.PostResponse;

import java.util.List;

public interface PostService {
    List<PostResponse> findAll();
    PostResponse findById(Long id);
    PostResponse create(PostRequest request);
    PostResponse update(long id, PostRequest request);
    void delete(long id);
    List<PostResponse> findByTitle(String title);
}
