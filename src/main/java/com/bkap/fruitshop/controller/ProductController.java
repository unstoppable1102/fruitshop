package com.bkap.fruitshop.controller;

import com.bkap.fruitshop.dto.request.ProductRequest;
import com.bkap.fruitshop.dto.response.ApiResponse;
import com.bkap.fruitshop.dto.response.ProductResponse;
import com.bkap.fruitshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@ModelAttribute ProductRequest request){
        try {
            ProductResponse productResponse = productService.createProduct(request);

            return ApiResponse.<ProductResponse>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(HttpStatus.CREATED.getReasonPhrase())
                    .result(productResponse)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> findAllProducts(){
        List<ProductResponse> products = productService.getAllProducts();
        return ApiResponse.<List<ProductResponse>>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(products)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable long id){
        try {
            ProductResponse product = productService.getProductById(id);
            return ApiResponse.<ProductResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(product)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @GetMapping("/search/{name}")
    public ApiResponse<List<ProductResponse>> findProductByName(@PathVariable String name){
        try {
            List<ProductResponse> productResponses = productService.findByProductName(name);
            return ApiResponse.<List<ProductResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .message(HttpStatus.OK.getReasonPhrase())
                    .result(productResponses)
                    .build();
        } catch (Exception e) {
            return ApiResponse.errorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable long id, @ModelAttribute ProductRequest request){
        ProductResponse updateProduct = productService.updateProduct(id, request);
        return ApiResponse.<ProductResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .result(updateProduct)
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
