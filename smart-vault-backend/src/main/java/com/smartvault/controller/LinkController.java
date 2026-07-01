package com.smartvault.controller;

import com.smartvault.dto.ApiResponse;
import com.smartvault.dto.LinkRequest;
import com.smartvault.dto.LinkResponse;
import com.smartvault.dto.PageResponse;
import com.smartvault.service.LinkService;
import com.smartvault.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping
    public ResponseEntity<ApiResponse<LinkResponse>> createLink(@Valid @RequestBody LinkRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        LinkResponse response = linkService.createLink(request, userId);
        return ResponseEntity.ok(ApiResponse.success("Link created", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<LinkResponse>>> getLinks(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean isFavorite,
            Pageable pageable
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<LinkResponse> page = linkService.getLinks(userId, category, isFavorite, pageable);
        return ResponseEntity.ok(ApiResponse.success("Links retrieved", PageResponse.of(page)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LinkResponse>> getLink(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        LinkResponse response = linkService.getLink(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Link retrieved", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LinkResponse>> updateLink(
            @PathVariable Long id,
            @Valid @RequestBody LinkRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        LinkResponse response = linkService.updateLink(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Link updated", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLink(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        linkService.deleteLink(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Link deleted"));
    }

    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse<LinkResponse>> toggleFavorite(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        LinkResponse response = linkService.toggleFavorite(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Favorite toggled", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<LinkResponse>>> searchLinks(@RequestParam String query) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<LinkResponse> responses = linkService.searchLinks(userId, query);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved", responses));
    }
}
