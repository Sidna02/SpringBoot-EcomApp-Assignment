package com.aymankhachchab.ecommerce.repository;

import com.aymankhachchab.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}