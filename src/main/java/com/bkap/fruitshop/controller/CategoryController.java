package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.CategoryRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.CategoryResponse;
import com.bkap.fruitshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> findAll(){
        List<CategoryResponse> categoryResponses = categoryService.findAll();
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(categoryResponses)
                .build();
    }

    @PostMapping
    public ApiResponse<CategoryResponse> create(@RequestBody CategoryRequest request){
        try {
            CategoryResponse categoryResponse = categoryService.save(request);
            return ApiResponse.<CategoryResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(categoryResponse)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> findById(@PathVariable long id){
        CategoryResponse categoryResponse = categoryService.findById(id);
        try {
            return ApiResponse.<CategoryResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(categoryResponse)
                    .build();
        }catch (Exception e){
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ApiResponse<List<CategoryResponse>> findByName(@PathVariable String name){
        List<CategoryResponse> categoryResponses = categoryService.findByName(name);
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(categoryResponses)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> update(@PathVariable long id, @RequestBody CategoryRequest request){
        try {
            CategoryResponse categoryResponse = categoryService.update(id, request);
            return ApiResponse.<CategoryResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(categoryResponse)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete( @PathVariable long id){
        try {
            categoryService.delete(id);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Category is deleted successfully!")
                    .build();
        }catch (Exception e){
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}

