package com.smartvault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "URL is required")
    @Size(max = 500)
    private String url;

    private String category;
    private String subcategory;
    private String description;
    private String notes;
}
