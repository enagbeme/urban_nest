package com.urbannest.backend.service;

import com.urbannest.backend.dto.AuthenticationResponse;
import com.urbannest.backend.dto.RegisterRequest;
import com.urbannest.backend.dto.VerificationRequest;
import com.urbannest.backend.entity.User;
import com.urbannest.backend.exception.EmailAlreadyTakenException;
import com.urbannest.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyTakenException("Email already taken");
        }
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true) // Auto-enable user
                .verificationCode(null)
                .verificationCodeExpiresAt(null)
                .build();
        var savedUser = repository.save(user);

        return AuthenticationResponse.builder()
                .role(savedUser.getRole())
                .fullName(savedUser.getFullName())
                .id(savedUser.getId())
                .message("Registration successful")
                .build();
    }

    public AuthenticationResponse verify(VerificationRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled()) {
             throw new RuntimeException("Account already verified");
        }

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code expired");
        }

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(request.getCode())) {
            throw new RuntimeException("Invalid verification code");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        repository.save(user);

        return AuthenticationResponse.builder()
                .role(user.getRole())
                .fullName(user.getFullName())
                .id(user.getId())
                .build();
    }
}
