package com.smartvault.service;

import com.smartvault.dto.UserResponse;
import com.smartvault.exception.BadRequestException;
import com.smartvault.exception.ResourceNotFoundException;
import com.smartvault.model.AuditLog;
import com.smartvault.model.DocumentCategory;
import com.smartvault.model.SharedLink;
import com.smartvault.model.User;
import com.smartvault.repository.AuditLogRepository;
import com.smartvault.repository.DocumentCategoryRepository;
import com.smartvault.repository.DocumentRepository;
import com.smartvault.repository.SharedLinkRepository;
import com.smartvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final SharedLinkRepository sharedLinkRepository;
    private final DocumentCategoryRepository categoryRepository;
    private final AuditLogRepository auditLogRepository;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::mapToUserResponse);
    }

    @Transactional
    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
    }

    public Map<String, Object> getSystemStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("activeUsers", userRepository.countByIsActive(true));
        stats.put("totalDocuments", documentRepository.count());
        stats.put("totalStorage", documentRepository.count()); // simplified
        stats.put("activeShares", sharedLinkRepository.countByIsRevoked(false));
        return stats;
    }

    public Page<AuditLog> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public List<DocumentCategory> getAllCategories() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    @Transactional
    public DocumentCategory createCategory(DocumentCategory category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new BadRequestException("Category already exists");
        }
        category.setIsSystem(false); // Only manual creation allowed here
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        DocumentCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (category.getIsSystem()) {
            throw new BadRequestException("System categories cannot be deleted");
        }
        categoryRepository.delete(category);
    }

    public Page<SharedLink> getSharedDocuments(Pageable pageable) {
        return sharedLinkRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
