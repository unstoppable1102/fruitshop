package com.bkap.fruitshop.service.impl;

import com.bkap.fruitshop.dto.request.CategoryRequest;
import com.bkap.fruitshop.dto.response.CategoryResponse;
import com.bkap.fruitshop.entity.Category;
import com.bkap.fruitshop.exception.AppException;
import com.bkap.fruitshop.exception.ErrorCode;
import com.bkap.fruitshop.repository.CategoryRepository;
import com.bkap.fruitshop.repository.ProductRepository;
import com.bkap.fruitshop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;


    @Override
    public CategoryResponse findById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map((element) -> modelMapper.map(element, CategoryResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse save(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = modelMapper.map(request, Category.class);
        return modelMapper.map(categoryRepository.save(category), CategoryResponse.class);
    }

    @Override
    public CategoryResponse update(long id, CategoryRequest request) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        modelMapper.map(request, existingCategory);
        Category updatedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryResponse.class);
    }

    @Override
    public void delete(long id) {
        boolean existsProduct = productRepository.existsByCategoryId(id);
        if (existsProduct) {
            throw new AppException(ErrorCode.PRODUCT_EXIST_IN_CATEGORY);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryResponse> findByName(String name) {
        List<Category> categoryList = categoryRepository.findByNameContainingIgnoreCase(name);
        return categoryList.stream()
                .map((element) -> modelMapper.map(element, CategoryResponse.class))
                .collect(Collectors.toList());
    }
}
