package com.aymankhachchab.ecommerce.service;

import com.aymankhachchab.ecommerce.dto.CreateCategoryDto;
import com.aymankhachchab.ecommerce.dto.ResponseCategoryDto;
import com.aymankhachchab.ecommerce.entity.Category;
import com.aymankhachchab.ecommerce.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseCategoryDto transformCategoryToDto(Category category) {
        return new ResponseCategoryDto(category.getName(), category.getDescription());
    }

    public List<Category> all() {
        return categoryRepository.findAll();
    }

    public Category createCategory(CreateCategoryDto createCategoryDto) {
        Category category = new Category();
        category.setName(createCategoryDto.getName());
        category.setDescription(createCategoryDto.getDescription());
        return categoryRepository.saveAndFlush(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public Category updateCategory(Long id, Category newCategory) {
        Category oldCategory = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        oldCategory.setName(newCategory.getName());
        oldCategory.setDescription(newCategory.getDescription());
        return categoryRepository.saveAndFlush(oldCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
