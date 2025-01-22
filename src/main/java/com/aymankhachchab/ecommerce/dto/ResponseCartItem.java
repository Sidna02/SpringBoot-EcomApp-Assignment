package com.aymankhachchab.ecommerce.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.aymankhachchab.ecommerce.entity.CartItem}
 */
@Value
public class ResponseCartItem implements Serializable {
    Long productId;
    String productName;
    int quantity;
}