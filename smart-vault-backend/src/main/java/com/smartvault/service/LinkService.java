package com.smartvault.service;

import com.smartvault.dto.LinkRequest;
import com.smartvault.dto.LinkResponse;
import com.smartvault.exception.ResourceNotFoundException;
import com.smartvault.model.ImportantLink;
import com.smartvault.model.User;
import com.smartvault.repository.ImportantLinkRepository;
import com.smartvault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final ImportantLinkRepository linkRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    @Transactional
    public LinkResponse createLink(LinkRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ImportantLink link = ImportantLink.builder()
                .user(user)
                .title(request.getTitle())
                .url(request.getUrl())
                .category(request.getCategory())
                .subcategory(request.getSubcategory())
                .description(request.getDescription())
                .notes(request.getNotes())
                .build();

        ImportantLink savedLink = linkRepository.save(link);
        auditService.log(userId, "CREATE_LINK", "LINK", savedLink.getId(), "Created link: " + link.getTitle(), "SYSTEM");

        return mapToResponse(savedLink);
    }

    public Page<LinkResponse> getLinks(Long userId, String category, Boolean isFavorite, Pageable pageable) {
        Page<ImportantLink> links;
        if (isFavorite != null && isFavorite) {
            return linkRepository.findByUserIdAndIsFavoriteOrderByCreatedAtDesc(userId, isFavorite)
                    .stream().map(this::mapToResponse)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), 
                           list -> new org.springframework.data.domain.PageImpl<>(list, pageable, list.size())));
        } else if (category != null && !category.isEmpty()) {
            links = linkRepository.findByUserIdAndCategoryOrderByCreatedAtDesc(userId, category, pageable);
        } else {
            links = linkRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        }
        return links.map(this::mapToResponse);
    }

    public LinkResponse getLink(Long id, Long userId) {
        ImportantLink link = linkRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found or access denied"));
        return mapToResponse(link);
    }

    @Transactional
    public LinkResponse updateLink(Long id, LinkRequest request, Long userId) {
        ImportantLink link = linkRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found or access denied"));

        if (request.getTitle() != null) link.setTitle(request.getTitle());
        if (request.getUrl() != null) link.setUrl(request.getUrl());
        if (request.getCategory() != null) link.setCategory(request.getCategory());
        if (request.getSubcategory() != null) link.setSubcategory(request.getSubcategory());
        if (request.getDescription() != null) link.setDescription(request.getDescription());
        if (request.getNotes() != null) link.setNotes(request.getNotes());

        ImportantLink updatedLink = linkRepository.save(link);
        auditService.log(userId, "UPDATE_LINK", "LINK", updatedLink.getId(), "Updated link: " + link.getTitle(), "SYSTEM");

        return mapToResponse(updatedLink);
    }

    @Transactional
    public void deleteLink(Long id, Long userId) {
        ImportantLink link = linkRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found or access denied"));
        linkRepository.delete(link);
        auditService.log(userId, "DELETE_LINK", "LINK", id, "Deleted link: " + link.getTitle(), "SYSTEM");
    }

    public List<LinkResponse> searchLinks(Long userId, String query) {
        return linkRepository.searchLinks(userId, query).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public LinkResponse toggleFavorite(Long id, Long userId) {
        ImportantLink link = linkRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found or access denied"));
        link.setIsFavorite(!link.getIsFavorite());
        return mapToResponse(linkRepository.save(link));
    }

    private LinkResponse mapToResponse(ImportantLink link) {
        return LinkResponse.builder()
                .id(link.getId())
                .title(link.getTitle())
                .url(link.getUrl())
                .category(link.getCategory())
                .subcategory(link.getSubcategory())
                .description(link.getDescription())
                .notes(link.getNotes())
                .isFavorite(link.getIsFavorite())
                .createdAt(link.getCreatedAt())
                .updatedAt(link.getUpdatedAt())
                .build();
    }
}
