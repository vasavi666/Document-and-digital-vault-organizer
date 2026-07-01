package com.smartvault.controller;

import com.smartvault.dto.ApiResponse;
import com.smartvault.dto.DocumentResponse;
import com.smartvault.dto.ShareRequest;
import com.smartvault.dto.ShareResponse;
import com.smartvault.service.ShareService;
import com.smartvault.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    @PostMapping("/share/documents/{documentId}")
    public ResponseEntity<ApiResponse<ShareResponse>> createShareLink(
            @PathVariable Long documentId,
            @RequestBody ShareRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        ShareResponse response = shareService.createShareLink(documentId, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Share link created", response));
    }

    // Public endpoint
    @GetMapping("/shared/{token}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getSharedDocument(@PathVariable String token) {
        DocumentResponse response = shareService.getSharedDocument(token);
        return ResponseEntity.ok(ApiResponse.success("Document retrieved successfully", response));
    }

    @DeleteMapping("/share/{id}")
    public ResponseEntity<ApiResponse<Void>> revokeShareLink(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        shareService.revokeShareLink(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Share link revoked"));
    }

    @GetMapping("/share/my-shares")
    public ResponseEntity<ApiResponse<List<ShareResponse>>> getUserShares() {
        Long userId = SecurityUtils.getCurrentUserId();
        List<ShareResponse> responses = shareService.getUserShares(userId);
        return ResponseEntity.ok(ApiResponse.success("Shares retrieved", responses));
    }
}
