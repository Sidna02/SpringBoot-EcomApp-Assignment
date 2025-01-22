package com.aymankhachchab.ecommerce.repository;

import com.aymankhachchab.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}