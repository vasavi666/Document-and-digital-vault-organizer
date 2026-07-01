package com.smartvault.auth;

import com.smartvault.dto.ApiResponse;
import com.smartvault.dto.AuthResponse;
import com.smartvault.dto.ForgotPasswordRequest;
import com.smartvault.dto.LoginRequest;
import com.smartvault.dto.RegisterRequest;
import com.smartvault.dto.ResetPasswordRequest;
import com.smartvault.dto.UserResponse;
import com.smartvault.model.User;
import com.smartvault.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Registration successful", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success("If the email exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully."));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        User user = SecurityUtils.getCurrentUser().getUser();
        UserResponse response = authService.mapToUserResponse(user);
        return ResponseEntity.ok(ApiResponse.success("Current user retrieved", response));
    }
    
    // Note: /refresh endpoint would typically use the refresh token to generate a new access token
    // For simplicity, we can implement it later or rely on the frontend to manage expiration.
}
