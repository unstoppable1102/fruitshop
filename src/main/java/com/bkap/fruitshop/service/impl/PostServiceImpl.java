package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.common.util.UploadFileUtil;
import com.bkap.fruitshop.dto.request.PostRequest;
import com.bkap.fruitshop.dto.response.PageResponse;
import com.bkap.fruitshop.dto.response.PostResponse;
import com.bkap.fruitshop.entity.Post;
import com.bkap.fruitshop.entity.PostCategory;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.PostCategoryRepository;
import com.bkap.fruitshop.repository.PostRepository;
import com.bkap.fruitshop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final UploadFileUtil uploadFileUtil;
    private final PostCategoryRepository postCategoryRepository;

    @Override
    public PageResponse<PostResponse> findAll(String keyword, Pageable pageable) {
        Page<Post> postPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            postPage = postRepository.findByTitleContainingIgnoreCase(keyword, pageable);
        }else {
            postPage = postRepository.findAll(pageable);
        }
        List<PostResponse> postResponses = postPage.getContent().stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .toList();
        return new PageResponse<>(postPage.getNumber(), postPage.getSize(),
                postPage.getTotalElements(), postPage.getTotalPages(), postPage.isLast(), postResponses);
    }

    @Override
    public PostResponse findById(Long id) {
        return modelMapper.map(postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND)), PostResponse.class);
    }

    @Override
    public PostResponse create(PostRequest request) {
        PostCategory postCategory = postCategoryRepository.findById(request.getPostCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_CATEGORY_NOT_FOUND));

        if (postRepository.existsByTitleIgnoreCase(request.getTitle())) {
            throw new AppException(ErrorCode.POST_EXISTED);
        }
        Post post = modelMapper.map(request, Post.class);
        post.setPostCategory(postCategory);
        post.setId(null);

        //Xu ly lưu file ảnh
        String imagePath = uploadFileUtil.saveImage(request.getImage());
        post.setImage(imagePath);
        return modelMapper.map(postRepository.save(post), PostResponse.class);
    }

    @Override
    public PostResponse update(long id, PostRequest request) {
        PostCategory postCategory = postCategoryRepository.findById(request.getPostCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_CATEGORY_NOT_FOUND));

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if(post.getTitle().equals(request.getTitle()) && postRepository.existsByTitleIgnoreCase(request.getTitle())) {
            throw new AppException(ErrorCode.POST_EXISTED);
        }
        modelMapper.map(request, post);
        post.setPostCategory(postCategory);

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            //save image into folder
            String imagePath = uploadFileUtil.saveImage(request.getImage());
            post.setImage(imagePath);
        }
        return modelMapper.map(postRepository.save(post), PostResponse.class);
    }

    @Override
    public void delete(long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (post.getImage() != null && !post.getImage().isEmpty()) {
            uploadFileUtil.deleteImage(post.getImage());
        }
        postRepository.delete(post);
    }

    @Override
    public List<PostResponse> findByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title).stream()
                .map((element) -> modelMapper.map(element, PostResponse.class))
                .collect(Collectors.toList());
    }
}
