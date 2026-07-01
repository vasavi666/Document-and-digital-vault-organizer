package com.smartvault.controller;

import com.smartvault.dto.ApiResponse;
import com.smartvault.dto.PageResponse;
import com.smartvault.dto.UserResponse;
import com.smartvault.model.AuditLog;
import com.smartvault.model.DocumentCategory;
import com.smartvault.model.SharedLink;
import com.smartvault.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(Pageable pageable) {
        Page<UserResponse> page = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved", PageResponse.of(page)));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<ApiResponse<Void>> toggleUserStatus(@PathVariable Long id) {
        adminService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.success("User status toggled"));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSystemStats() {
        Map<String, Object> stats = adminService.getSystemStats();
        return ResponseEntity.ok(ApiResponse.success("System stats retrieved", stats));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<PageResponse<AuditLog>>> getAuditLogs(Pageable pageable) {
        Page<AuditLog> page = adminService.getAuditLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved", PageResponse.of(page)));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<DocumentCategory>>> getAllCategories() {
        List<DocumentCategory> categories = adminService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved", categories));
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<DocumentCategory>> createCategory(@RequestBody DocumentCategory category) {
        DocumentCategory createdCategory = adminService.createCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Category created", createdCategory));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        adminService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted"));
    }

    @GetMapping("/shared-documents")
    public ResponseEntity<ApiResponse<PageResponse<SharedLink>>> getSharedDocuments(Pageable pageable) {
        Page<SharedLink> page = adminService.getSharedDocuments(pageable);
        return ResponseEntity.ok(ApiResponse.success("Shared documents retrieved", PageResponse.of(page)));
    }
}
