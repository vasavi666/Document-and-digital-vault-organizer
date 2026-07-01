package com.smartvault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardResponse {
    private long totalDocuments;
    private long totalLinks;
    private long expiringSoon;
    private long storageUsed;
    private List<DocumentResponse> recentUploads;
    private List<DocumentResponse> expiringDocuments;
    private List<LinkResponse> favoriteLinks;
    private List<Map<String, Object>> categoryBreakdown;
}
