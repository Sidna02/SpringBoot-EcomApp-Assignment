package com.aymankhachchab.ecommerce.service;

import com.aymankhachchab.ecommerce.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationService {
    @Autowired
    private CartService cartService;

    public boolean isOrderOwner(String username, Long orderId) {
        Order order = cartService.getOrderById(orderId);
        if (order == null) {
            return false;
        }
        return order.getUser().getUsername().equals(username);
    }
}
