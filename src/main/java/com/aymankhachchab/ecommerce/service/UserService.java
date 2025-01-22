package com.aymankhachchab.ecommerce.service;

import com.aymankhachchab.ecommerce.dto.ResponseUserDto;
import com.aymankhachchab.ecommerce.entity.User;
import com.aymankhachchab.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public UserService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        users.addAll(userRepository.findAll());

        return users;
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByEmail(username).orElse(null);
    }

    public String getAuthenticatedUsername() {
        return this.authenticationService.getAuthenticatedUsername();
    }

    public User getAuthenticatedUser() {
        return this.userRepository.findByEmail(getAuthenticatedUsername()).orElseThrow();
    }

    public ResponseUserDto transferUserToDto(User user) {
        return new ResponseUserDto(
                user.getName(),
                user.getUsername(),
                user.getRole(),
                user.getAddress()

        );
    }
}