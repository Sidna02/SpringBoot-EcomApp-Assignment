package com.aymankhachchab.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.aymankhachchab.ecommerce.entity.Category}
 */
@Value
@AllArgsConstructor
public class ResponseCategoryDto implements Serializable {
    String name;
    String description;
}