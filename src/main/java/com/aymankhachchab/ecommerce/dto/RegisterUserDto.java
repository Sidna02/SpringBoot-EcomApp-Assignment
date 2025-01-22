package com.aymankhachchab.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    @Email
    private String email;
    @NotEmpty
    private String password;

    private String fullName;

}
