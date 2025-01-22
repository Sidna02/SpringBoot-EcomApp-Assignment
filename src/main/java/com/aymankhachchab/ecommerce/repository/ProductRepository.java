package com.aymankhachchab.ecommerce.repository;

import com.aymankhachchab.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}