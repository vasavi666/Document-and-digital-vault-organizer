package com.smartvault.service;

import com.smartvault.dto.DocumentRequest;
import com.smartvault.dto.DocumentResponse;
import com.smartvault.exception.ResourceNotFoundException;
import com.smartvault.exception.UnauthorizedException;
import com.smartvault.model.Document;
import com.smartvault.model.DocumentCategory;
import com.smartvault.model.User;
import com.smartvault.repository.DocumentCategoryRepository;
import com.smartvault.repository.DocumentRepository;
import com.smartvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentCategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final OcrService ocrService;
    private final AuditService auditService;

    @Transactional
    public DocumentResponse uploadDocument(MultipartFile file, DocumentRequest request, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        DocumentCategory category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        }

        Map uploadResult = cloudinaryService.uploadFile(file);
        String fileUrl = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        Document document = Document.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(category)
                .fileUrl(fileUrl)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .cloudinaryPublicId(publicId)
                .folder(request.getFolder() != null ? request.getFolder() : "General")
                .tags(request.getTags())
                .expiryDate(request.getExpiryDate())
                .renewalDate(request.getRenewalDate())
                .build();

        Document savedDocument = documentRepository.save(document);
        
        auditService.log(userId, "UPLOAD_DOCUMENT", "DOCUMENT", savedDocument.getId(), "Uploaded document: " + document.getTitle(), "SYSTEM");

        try {
            byte[] fileBytes = file.getBytes();
            ocrService.processDocument(savedDocument, fileBytes);
        } catch (Exception e) {
            log.error("Failed to read file bytes for OCR: {}", e.getMessage());
        }

        return mapToResponse(savedDocument);
    }

    public Page<DocumentResponse> getDocuments(Long userId, Long categoryId, String folder, Pageable pageable) {
        Page<Document> documents;
        if (categoryId != null) {
            documents = documentRepository.findByUserIdAndCategoryIdOrderByCreatedAtDesc(userId, categoryId, pageable);
        } else if (folder != null && !folder.isEmpty()) {
            documents = documentRepository.findByUserIdAndFolderOrderByCreatedAtDesc(userId, folder, pageable);
        } else {
            documents = documentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        }
        return documents.map(this::mapToResponse);
    }

    public DocumentResponse getDocument(Long id, Long userId) {
        Document document = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found or access denied"));
        return mapToResponse(document);
    }

    @Transactional
    public DocumentResponse updateDocument(Long id, DocumentRequest request, Long userId) {
        Document document = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found or access denied"));

        if (request.getCategoryId() != null && (document.getCategory() == null || !document.getCategory().getId().equals(request.getCategoryId()))) {
            DocumentCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            document.setCategory(category);
        } else if (request.getCategoryId() == null) {
            document.setCategory(null);
        }

        if (request.getTitle() != null) document.setTitle(request.getTitle());
        if (request.getDescription() != null) document.setDescription(request.getDescription());
        if (request.getFolder() != null) document.setFolder(request.getFolder());
        if (request.getTags() != null) document.setTags(request.getTags());
        if (request.getExpiryDate() != null) document.setExpiryDate(request.getExpiryDate());
        if (request.getRenewalDate() != null) document.setRenewalDate(request.getRenewalDate());

        Document updatedDocument = documentRepository.save(document);
        
        auditService.log(userId, "UPDATE_DOCUMENT", "DOCUMENT", updatedDocument.getId(), "Updated document: " + updatedDocument.getTitle(), "SYSTEM");

        return mapToResponse(updatedDocument);
    }

    @Transactional
    public void deleteDocument(Long id, Long userId) {
        Document document = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found or access denied"));

        if (document.getCloudinaryPublicId() != null) {
            cloudinaryService.deleteFile(document.getCloudinaryPublicId());
        }

        documentRepository.delete(document);
        
        auditService.log(userId, "DELETE_DOCUMENT", "DOCUMENT", id, "Deleted document: " + document.getTitle(), "SYSTEM");
    }

    public Page<DocumentResponse> searchDocuments(Long userId, String query, Pageable pageable) {
        return documentRepository.searchDocuments(userId, query, pageable).map(this::mapToResponse);
    }

    public List<DocumentResponse> getExpiringDocuments(Long userId, int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        return documentRepository.findExpiringDocuments(userId, startDate, endDate)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public DocumentResponse toggleFavorite(Long id, Long userId) {
        Document document = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found or access denied"));
        
        document.setIsFavorite(!document.getIsFavorite());
        return mapToResponse(documentRepository.save(document));
    }

    private DocumentResponse mapToResponse(Document document) {
        String ocrStatusStr = document.getOcrData() != null ? document.getOcrData().getStatus().name() : "NONE";
        String ocrTextStr = document.getOcrData() != null ? document.getOcrData().getExtractedText() : null;

        return DocumentResponse.builder()
                .id(document.getId())
                .title(document.getTitle())
                .description(document.getDescription())
                .categoryId(document.getCategory() != null ? document.getCategory().getId() : null)
                .categoryName(document.getCategory() != null ? document.getCategory().getName() : null)
                .categoryIcon(document.getCategory() != null ? document.getCategory().getIcon() : null)
                .fileUrl(document.getFileUrl())
                .fileName(document.getFileName())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .folder(document.getFolder())
                .tags(document.getTags())
                .expiryDate(document.getExpiryDate())
                .renewalDate(document.getRenewalDate())
                .isFavorite(document.getIsFavorite())
                .ocrStatus(ocrStatusStr)
                .ocrText(ocrTextStr)
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
