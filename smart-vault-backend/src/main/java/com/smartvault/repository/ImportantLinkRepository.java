package com.smartvault.repository;

import com.smartvault.model.ImportantLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImportantLinkRepository extends JpaRepository<ImportantLink, Long> {

    Page<ImportantLink> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<ImportantLink> findByUserIdAndCategoryOrderByCreatedAtDesc(Long userId, String category, Pageable pageable);

    List<ImportantLink> findByUserIdAndIsFavoriteOrderByCreatedAtDesc(Long userId, Boolean isFavorite);

    Optional<ImportantLink> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT l FROM ImportantLink l WHERE l.user.id = :userId AND " +
           "(LOWER(l.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.url) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<ImportantLink> searchLinks(@Param("userId") Long userId, @Param("query") String query);

    long countByUserId(Long userId);
}
