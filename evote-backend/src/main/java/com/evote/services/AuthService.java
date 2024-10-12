// AuthService.java (update)
package com.evote.services;

import com.evote.dto.AuthResponse;
import com.evote.dto.LoginRequest;
import com.evote.dto.RegisterRequest;
import com.evote.models.User;
import com.evote.repositories.UserRepository;
import com.evote.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER") // Default role for new registrations
                .build();

        userRepository.save(user);
        var jwt = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwt)
                .role(user.getRole())
                .message("User registered successfully")
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = (User) authentication.getPrincipal();
        var jwt = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwt)
                .role(user.getRole())
                .message("Login successful")
                .build();
    }

    @Transactional
    public void createAdminUser(String email, String password) {
        if (!userRepository.existsByEmail(email)) {
            var adminUser = User.builder()
                    .fullName("Admin User")
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .role("ROLE_ADMIN")
                    .build();
            userRepository.save(adminUser);
        }
    }
}
