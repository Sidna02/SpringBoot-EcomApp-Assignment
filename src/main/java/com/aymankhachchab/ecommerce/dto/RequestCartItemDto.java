package com.aymankhachchab.ecommerce.dto;

import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.aymankhachchab.ecommerce.entity.CartItem}
 */
@Value
public class RequestCartItemDto implements Serializable {
    Long productId;
    @Positive
    int quantity;
}