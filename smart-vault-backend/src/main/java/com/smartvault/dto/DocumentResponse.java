package com.smartvault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {
    private Long id;
    private String title;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private String fileUrl;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String folder;
    private String tags;
    private LocalDate expiryDate;
    private LocalDate renewalDate;
    private Boolean isFavorite;
    private String ocrStatus;
    private String ocrText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
