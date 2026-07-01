package com.smartvault.service;

import com.smartvault.dto.DashboardResponse;
import com.smartvault.dto.DocumentResponse;
import com.smartvault.dto.LinkResponse;
import com.smartvault.repository.DocumentRepository;
import com.smartvault.repository.ImportantLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DocumentRepository documentRepository;
    private final ImportantLinkRepository linkRepository;

    public DashboardResponse getDashboard(Long userId) {
        long totalDocuments = documentRepository.countByUserId(userId);
        long totalLinks = linkRepository.countByUserId(userId);
        
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysFromNow = today.plusDays(30);
        List<DocumentResponse> expiringDocs = documentRepository.findExpiringDocuments(userId, today, thirtyDaysFromNow)
                .stream().map(d -> {
                    return DocumentResponse.builder()
                            .id(d.getId())
                            .title(d.getTitle())
                            .categoryName(d.getCategory() != null ? d.getCategory().getName() : null)
                            .fileType(d.getFileType())
                            .expiryDate(d.getExpiryDate())
                            .build();
                }).collect(Collectors.toList());
        long expiringSoon = expiringDocs.size();
        
        long storageUsed = documentRepository.sumFileSizeByUserId(userId);

        List<DocumentResponse> recentUploads = documentRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(d -> {
                    return DocumentResponse.builder()
                            .id(d.getId())
                            .title(d.getTitle())
                            .categoryName(d.getCategory() != null ? d.getCategory().getName() : null)
                            .fileType(d.getFileType())
                            .fileSize(d.getFileSize())
                            .createdAt(d.getCreatedAt())
                            .build();
                }).collect(Collectors.toList());

        List<LinkResponse> favoriteLinks = linkRepository.findByUserIdAndIsFavoriteOrderByCreatedAtDesc(userId, true)
                .stream().limit(5).map(l -> {
                    return LinkResponse.builder()
                            .id(l.getId())
                            .title(l.getTitle())
                            .url(l.getUrl())
                            .isFavorite(l.getIsFavorite())
                            .build();
                }).collect(Collectors.toList());

        List<Object[]> categoryCounts = documentRepository.countByCategory(userId);
        List<Map<String, Object>> categoryBreakdown = new ArrayList<>();
        for (Object[] count : categoryCounts) {
            Map<String, Object> map = new HashMap<>();
            map.put("category", count[0]);
            map.put("count", count[1]);
            categoryBreakdown.add(map);
        }

        return DashboardResponse.builder()
                .totalDocuments(totalDocuments)
                .totalLinks(totalLinks)
                .expiringSoon(expiringSoon)
                .storageUsed(storageUsed)
                .recentUploads(recentUploads)
                .expiringDocuments(expiringDocs)
                .favoriteLinks(favoriteLinks)
                .categoryBreakdown(categoryBreakdown)
                .build();
    }
}
