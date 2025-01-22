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
    Long id;
    String name;
    String description;
}