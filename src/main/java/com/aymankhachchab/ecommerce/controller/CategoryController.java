package com.aymankhachchab.ecommerce.controller;

import com.aymankhachchab.ecommerce.dto.CreateCategoryDto;
import com.aymankhachchab.ecommerce.dto.ResponseCategoryDto;
import com.aymankhachchab.ecommerce.entity.Category;
import com.aymankhachchab.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public List<Category> allCategories() {
        return this.categoryService.all();
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCategoryDto> newCategory(@Valid @RequestBody CreateCategoryDto newCategory) {
        Category category = categoryService.createCategory(newCategory);
        ResponseCategoryDto response = categoryService.transformCategoryToDto(category);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")

    public Category one(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public Category replaceCategory(@RequestBody Category newCategory, @PathVariable Long id) {
        return categoryService.updateCategory(id, newCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
