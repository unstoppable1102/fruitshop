package com.bkap.fruitshop.service;

import com.bkap.fruitshop.dto.request.PostRequest;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.PostResponse;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PageResponse<PostResponse> findAll(String keyword, Pageable pageable);
    PostResponse findById(Long id);
    PostResponse create(PostRequest request);
    PostResponse update(long id, PostRequest request);
    void delete(long id);
}
