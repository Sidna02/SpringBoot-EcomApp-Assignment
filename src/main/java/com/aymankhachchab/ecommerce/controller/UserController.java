package com.aymankhachchab.ecommerce.controller;

import com.aymankhachchab.ecommerce.dto.ResponseUserDto;
import com.aymankhachchab.ecommerce.entity.User;
import com.aymankhachchab.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseUserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(this.userService.transferUserToDto(currentUser));
    }

    @GetMapping("/")
    public ResponseEntity<List<ResponseUserDto>> allUsers() {

        List<ResponseUserDto> responseUserDtos = new ArrayList<>();
        userService.allUsers().forEach(user -> responseUserDtos.add(this.userService.transferUserToDto(user)));

        return ResponseEntity.ok(responseUserDtos);
    }
}