package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.ProductRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.ProductResponse;
import com.bkap.fruitshop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@Valid @ModelAttribute ProductRequest request, BindingResult result){
        if(result.hasErrors()){
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), result.getFieldError().getDefaultMessage());
        }
            return ApiResponse.<ProductResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(productService.createProduct(request))
                    .build();
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> findAllProducts(){
        return ApiResponse.<List<ProductResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(productService.getAllProducts())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable long id){
        try {
            return ApiResponse.<ProductResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(productService.getProductById(id))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ApiResponse<List<ProductResponse>> findProductByName(@PathVariable String name){
        try {
            return ApiResponse.<List<ProductResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(productService.findByProductName(name))
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@Valid @PathVariable long id, @ModelAttribute ProductRequest request, BindingResult result){
        if(result.hasErrors()){
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), result.getFieldError().getDefaultMessage());
        }
            return ApiResponse.<ProductResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(productService.updateProduct(id, request))
                    .build();

    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProduct( @PathVariable long id){
        try {
            productService.deleteProduct(id);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.NO_CONTENT.value())
                    .message("Product is deleted successfully!")
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }

    }
}
