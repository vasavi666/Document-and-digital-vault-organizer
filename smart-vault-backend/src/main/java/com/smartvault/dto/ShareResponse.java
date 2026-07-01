package com.smartvault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ShareResponse {
    private Long id;
    private Long documentId;
    private String documentTitle;
    private String shareToken;
    private String shareUrl;
    private LocalDateTime expiresAt;
    private Boolean isRevoked;
    private Integer accessCount;
    private LocalDateTime createdAt;
}
