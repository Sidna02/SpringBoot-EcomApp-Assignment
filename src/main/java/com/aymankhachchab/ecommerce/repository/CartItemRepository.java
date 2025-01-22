package com.aymankhachchab.ecommerce.repository;

import com.aymankhachchab.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}