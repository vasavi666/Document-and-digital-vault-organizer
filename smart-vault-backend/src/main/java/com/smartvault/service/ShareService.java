package com.smartvault.service;

import com.smartvault.dto.DocumentResponse;
import com.smartvault.dto.ShareRequest;
import com.smartvault.dto.ShareResponse;
import com.smartvault.exception.BadRequestException;
import com.smartvault.exception.ResourceNotFoundException;
import com.smartvault.exception.UnauthorizedException;
import com.smartvault.model.Document;
import com.smartvault.model.SharedLink;
import com.smartvault.model.User;
import com.smartvault.repository.DocumentRepository;
import com.smartvault.repository.SharedLinkRepository;
import com.smartvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final SharedLinkRepository sharedLinkRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Transactional
    public ShareResponse createShareLink(Long documentId, ShareRequest request, Long userId) {
        Document document = documentRepository.findByIdAndUserId(documentId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found or access denied"));

        User user = userRepository.findById(userId).orElseThrow();

        String token = "sv-share-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        SharedLink sharedLink = SharedLink.builder()
                .document(document)
                .sharedBy(user)
                .shareToken(token)
                .expiresAt(request.getExpiresAt())
                .isRevoked(false)
                .accessCount(0)
                .build();

        SharedLink savedLink = sharedLinkRepository.save(sharedLink);
        auditService.log(userId, "SHARE_DOCUMENT", "SHARED_LINK", savedLink.getId(), "Shared document: " + document.getTitle(), "SYSTEM");

        return mapToResponse(savedLink);
    }

    @Transactional
    public DocumentResponse getSharedDocument(String token) {
        SharedLink link = sharedLinkRepository.findByShareToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Share link not found"));

        if (link.getIsRevoked()) {
            throw new BadRequestException("This share link has been revoked");
        }

        if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("This share link has expired");
        }

        link.setAccessCount(link.getAccessCount() + 1);
        sharedLinkRepository.save(link);

        Document document = link.getDocument();
        return DocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .fileUrl(document.getFileUrl())
                .fileName(document.getFileName())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .build();
    }

    @Transactional
    public void revokeShareLink(Long shareId, Long userId) {
        SharedLink link = sharedLinkRepository.findById(shareId)
                .orElseThrow(() -> new ResourceNotFoundException("Share link not found"));

        if (!link.getSharedBy().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to revoke this link");
        }

        link.setIsRevoked(true);
        sharedLinkRepository.save(link);
        auditService.log(userId, "REVOKE_SHARE", "SHARED_LINK", shareId, "Revoked share link for document: " + link.getDocument().getTitle(), "SYSTEM");
    }

    public List<ShareResponse> getUserShares(Long userId) {
        return sharedLinkRepository.findBySharedByIdOrderByCreatedAtDesc(userId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private ShareResponse mapToResponse(SharedLink link) {
        return ShareResponse.builder()
                .id(link.getId())
                .documentId(link.getDocument().getId())
                .documentTitle(link.getDocument().getTitle())
                .shareToken(link.getShareToken())
                .shareUrl(frontendUrl + "/shared/" + link.getShareToken())
                .expiresAt(link.getExpiresAt())
                .isRevoked(link.getIsRevoked())
                .accessCount(link.getAccessCount())
                .createdAt(link.getCreatedAt())
                .build();
    }
}
