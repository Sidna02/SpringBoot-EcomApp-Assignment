package com.aymankhachchab.ecommerce.service;

import com.aymankhachchab.ecommerce.dto.CreateCategoryDto;
import com.aymankhachchab.ecommerce.dto.ResponseCategoryDto;
import com.aymankhachchab.ecommerce.entity.Category;
import com.aymankhachchab.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

     
    public ResponseCategoryDto transformCategoryToDto(Category category) {
        return new ResponseCategoryDto(category.getId(), category.getName(), category.getDescription());
    }

     
    public List<ResponseCategoryDto> all() {
        return categoryRepository.findAll().stream()
                .map(this::transformCategoryToDto)
                .collect(Collectors.toList());
    }

     
    public ResponseCategoryDto createCategory(CreateCategoryDto createCategoryDto) {
        Category category = new Category();
        category.setName(createCategoryDto.getName());
        category.setDescription(createCategoryDto.getDescription());
        Category savedCategory = categoryRepository.saveAndFlush(category);
        return transformCategoryToDto(savedCategory);   
    }

     
    public ResponseCategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return transformCategoryToDto(category);
    }

     
    public ResponseCategoryDto updateCategory(Long id, Category newCategoryDetails) {
        Category oldCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        oldCategory.setName(newCategoryDetails.getName());
        oldCategory.setDescription(newCategoryDetails.getDescription());

        Category updatedCategory = categoryRepository.saveAndFlush(oldCategory);
        return transformCategoryToDto(updatedCategory);   
    }

     
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
