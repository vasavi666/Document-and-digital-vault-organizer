package com.smartvault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartvault.dto.ApiResponse;
import com.smartvault.dto.DocumentRequest;
import com.smartvault.dto.DocumentResponse;
import com.smartvault.dto.PageResponse;
import com.smartvault.service.DocumentService;
import com.smartvault.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("metadata") String metadataJson
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        DocumentRequest request = mapper.readValue(metadataJson, DocumentRequest.class);
        
        Long userId = SecurityUtils.getCurrentUserId();
        DocumentResponse response = documentService.uploadDocument(file, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Document uploaded successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DocumentResponse>>> getDocuments(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String folder,
            Pageable pageable
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<DocumentResponse> page = documentService.getDocuments(userId, categoryId, folder, pageable);
        return ResponseEntity.ok(ApiResponse.success("Documents retrieved", PageResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        DocumentResponse response = documentService.getDocument(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Document retrieved", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponse>> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        DocumentResponse response = documentService.updateDocument(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Document updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        documentService.deleteDocument(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Document deleted"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<DocumentResponse>>> searchDocuments(
            @RequestParam String query,
            Pageable pageable
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<DocumentResponse> page = documentService.searchDocuments(userId, query, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved", PageResponse.of(page)));
    }

    @GetMapping("/expiring")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getExpiringDocuments(
            @RequestParam(defaultValue = "30") int days
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<DocumentResponse> responses = documentService.getExpiringDocuments(userId, days);
        return ResponseEntity.ok(ApiResponse.success("Expiring documents retrieved", responses));
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse<DocumentResponse>> toggleFavorite(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        DocumentResponse response = documentService.toggleFavorite(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Favorite toggled", response));
    }
}
