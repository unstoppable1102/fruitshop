package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.PostCategoryRequest;
import com.bkap.fruitshop.dto.response.PostCategoryResponse;
import com.bkap.fruitshop.entity.PostCategory;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.PostCategoryRepository;
import com.bkap.fruitshop.repository.PostRepository;
import com.bkap.fruitshop.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;

    @Override
    public List<PostCategoryResponse> findAll() {
        return postCategoryRepository.findAll().stream()
                .map(e -> modelMapper.map(e, PostCategoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostCategoryResponse> findByName(String name) {
        return postCategoryRepository.findByNameContainingIgnoreCase(name).stream()
                .map(e -> modelMapper.map(e, PostCategoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostCategoryResponse findById(Long id) {
        return modelMapper.map(postCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_CATEGORY_NOT_FOUND)), PostCategoryResponse.class);
    }

    @Override
    public PostCategoryResponse create(PostCategoryRequest request) {
        if (postCategoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.POST_CATEGORY_EXISTED);
        }

        PostCategory postCategory = modelMapper.map(request, PostCategory.class);
        return modelMapper.map(postCategoryRepository.save(postCategory), PostCategoryResponse.class);
    }

    @Override
    public PostCategoryResponse update(long id, PostCategoryRequest request) {
        PostCategory existingPostCategory = postCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_CATEGORY_NOT_FOUND));
        modelMapper.map(request, existingPostCategory);
        return modelMapper.map(postCategoryRepository.save(existingPostCategory), PostCategoryResponse.class);
    }

    @Override
    public void delete(long id) {
        PostCategory postCategory = postCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_CATEGORY_NOT_FOUND));
        boolean existsPost = postRepository.existsByPostCategoryId(id);
        if (existsPost) {
            throw new AppException(ErrorCode.POST_EXIST_IN_POST_CATEGORY);
        }
        postCategoryRepository.delete(postCategory);
    }
}
