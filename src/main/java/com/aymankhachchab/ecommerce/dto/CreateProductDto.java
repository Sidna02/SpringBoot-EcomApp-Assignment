package com.aymankhachchab.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.aymankhachchab.ecommerce.entity.Product}
 */
@Value
@ToString
@Getter
@Setter
public class CreateProductDto implements Serializable {
    @NotNull
    @NotEmpty
    String name;
    String description;
    @Positive
    BigDecimal price;
    @PositiveOrZero
    int stockQuantity;
    @Positive
    Long categoryId;
}