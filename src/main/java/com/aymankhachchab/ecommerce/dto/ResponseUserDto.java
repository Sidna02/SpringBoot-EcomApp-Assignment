package com.aymankhachchab.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.aymankhachchab.ecommerce.entity.User}
 */
@Value
public class ResponseUserDto implements Serializable {

    String name;
    @Email
    @NotBlank
    String email;
    String role;
    String address;
}