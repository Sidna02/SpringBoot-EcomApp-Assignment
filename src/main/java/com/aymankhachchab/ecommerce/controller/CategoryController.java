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
    public ResponseEntity<List<ResponseCategoryDto>> allCategories() {
        List<ResponseCategoryDto> categories = categoryService.all();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCategoryDto> newCategory(@Valid @RequestBody CreateCategoryDto newCategory) {
        ResponseCategoryDto category = categoryService.createCategory(newCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCategoryDto> getCategoryById(@PathVariable Long id) {
        try {
            ResponseCategoryDto category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCategoryDto> replaceCategory(@RequestBody Category newCategory, @PathVariable Long id) {
        try {
            ResponseCategoryDto updatedCategory = categoryService.updateCategory(id, newCategory);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
