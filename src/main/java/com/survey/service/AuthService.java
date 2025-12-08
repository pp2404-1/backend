package com.survey.service;

import com.survey.dto.AuthRequest;
import com.survey.dto.AuthResponse;
import com.survey.dto.UserDTO;
import com.survey.entity.User;
import com.survey.repository.UserRepository;
import com.survey.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service
 * Handles user registration and login
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Register a new user
     */
    @Transactional
    public AuthResponse register(AuthRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.UserRole.USER)
                .active(true)
                .build();

        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        java.util.Collections.emptyList()
                )
        );

        // Build response
        UserDTO userDTO = UserDTO.builder()
                .id("user-" + user.getId())
                .username(user.getUsername())
                .token(token)
                .build();

        return AuthResponse.builder()
                .user(userDTO)
                .token(token)
                .message("Registration successful")
                .build();
    }

    /**
     * Login user
     */
    public AuthResponse login(AuthRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get user from database
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate JWT token
            String token = jwtUtil.generateToken(
                    new org.springframework.security.core.userdetails.User(
                            user.getUsername(),
                            user.getPassword(),
                            java.util.Collections.emptyList()
                    )
            );

            // Build response
            UserDTO userDTO = UserDTO.builder()
                    .id("user-" + user.getId())
                    .username(user.getUsername())
                    .token(token)
                    .build();

            return AuthResponse.builder()
                    .user(userDTO)
                    .token(token)
                    .message("Login successful")
                    .build();

        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }
}

