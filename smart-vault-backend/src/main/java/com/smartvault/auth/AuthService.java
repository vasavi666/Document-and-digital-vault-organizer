package com.smartvault.auth;

import com.smartvault.dto.AuthResponse;
import com.smartvault.dto.ForgotPasswordRequest;
import com.smartvault.dto.LoginRequest;
import com.smartvault.dto.RegisterRequest;
import com.smartvault.dto.ResetPasswordRequest;
import com.smartvault.dto.UserResponse;
import com.smartvault.exception.BadRequestException;
import com.smartvault.exception.ResourceNotFoundException;
import com.smartvault.model.RefreshToken;
import com.smartvault.model.User;
import com.smartvault.model.enums.NotificationType;
import com.smartvault.repository.RefreshTokenRepository;
import com.smartvault.repository.UserRepository;
import com.smartvault.security.CustomUserDetails;
import com.smartvault.security.JwtService;
import com.smartvault.service.AuditService;
import com.smartvault.service.EmailService;
import com.smartvault.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final NotificationService notificationService;
    private final AuditService auditService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshExpiration;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        
        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        saveUserRefreshToken(savedUser, refreshToken);

        // Send welcome email and notification asynchronously (or rely on @Async if configured in methods)
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFullName());
        notificationService.createNotification(savedUser.getId(), NotificationType.WELCOME, "Welcome to Smart Vault!", "Thank you for joining Smart Vault. Start by uploading your first document or saving an important link.");
        auditService.log(savedUser.getId(), "REGISTER", "USER", savedUser.getId(), "User registered successfully", "SYSTEM");

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(mapToUserResponse(savedUser))
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getIsActive()) {
            throw new BadRequestException("User account is disabled");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        revokeAllUserRefreshTokens(user);
        saveUserRefreshToken(user, refreshToken);
        
        auditService.log(user.getId(), "LOGIN", "USER", user.getId(), "User logged in successfully", "SYSTEM"); // IP could be extracted from request

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(mapToUserResponse(user))
                .build();
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with this email"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String resetLink = frontendUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        
        auditService.log(user.getId(), "FORGOT_PASSWORD", "USER", user.getId(), "Password reset requested", "SYSTEM");
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findAll().stream()
                .filter(u -> request.getToken().equals(u.getResetToken()) && 
                        u.getResetTokenExpiry() != null && 
                        u.getResetTokenExpiry().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        
        auditService.log(user.getId(), "RESET_PASSWORD", "USER", user.getId(), "Password reset successfully", "SYSTEM");
    }

    private void saveUserRefreshToken(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    private void revokeAllUserRefreshTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .avatarUrl(user.getAvatarUrl())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
