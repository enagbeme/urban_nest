package com.urbannest.backend.controller;

import com.urbannest.backend.dto.AuthenticationResponse;
import com.urbannest.backend.dto.RegisterRequest;
import com.urbannest.backend.dto.VerificationRequest;
import com.urbannest.backend.entity.User;
import com.urbannest.backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @GetMapping("/me")
    public ResponseEntity<AuthenticationResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .role(user.getRole())
                .email(user.getEmail())
                .build());
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse authResponse = service.register(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verify(
            @RequestBody VerificationRequest request
    ) {
        AuthenticationResponse authResponse = service.verify(request);
        return ResponseEntity.ok(authResponse);
    }
}
