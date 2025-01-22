package com.aymankhachchab.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class ResponseProductDto implements Serializable {
    String name;
    String description;
    BigDecimal price;
    int stockQuantity;
    LocalDateTime createdAt;
    String categoryName;


}
