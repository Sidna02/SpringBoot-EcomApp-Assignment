package com.aymankhachchab.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.aymankhachchab.ecommerce.entity.Category}
 */
@Value
public class CreateCategoryDto implements Serializable {
    @NotNull
    @NotEmpty
    String name;

    String description;
}