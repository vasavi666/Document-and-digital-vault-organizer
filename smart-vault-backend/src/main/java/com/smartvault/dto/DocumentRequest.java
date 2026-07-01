package com.smartvault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentRequest {
    private String title;
    private String description;
    private Long categoryId;
    private String folder;
    private String tags;
    private LocalDate expiryDate;
    private LocalDate renewalDate;
}
