package com.aymankhachchab.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.aymankhachchab.ecommerce.entity.Order}
 */
@Value
@AllArgsConstructor
public class ResponseOrderDto implements Serializable {
    Long orderId;
    String userEmail;
    BigDecimal totalAmount;
    String status;
    List<OrderItemDto> orderItems;

    /**
     * DTO for {@link com.aymankhachchab.ecommerce.entity.OrderItem}
     */
    @Value
    public static class OrderItemDto implements Serializable {
        Long id;
        Long productId;
        String productName;
        int quantity;
        double price;
    }

}