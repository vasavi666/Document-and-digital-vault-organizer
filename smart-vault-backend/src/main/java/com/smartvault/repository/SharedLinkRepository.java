package com.smartvault.repository;

import com.smartvault.model.SharedLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SharedLinkRepository extends JpaRepository<SharedLink, Long> {
    Optional<SharedLink> findByShareToken(String shareToken);
    List<SharedLink> findBySharedByIdOrderByCreatedAtDesc(Long userId);
    List<SharedLink> findByDocumentId(Long documentId);
    Page<SharedLink> findAllByOrderByCreatedAtDesc(Pageable pageable);
    long countByIsRevoked(boolean isRevoked);
}
