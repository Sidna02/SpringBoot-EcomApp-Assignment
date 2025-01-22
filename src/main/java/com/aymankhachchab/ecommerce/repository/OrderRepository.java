package com.aymankhachchab.ecommerce.repository;

import com.aymankhachchab.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}