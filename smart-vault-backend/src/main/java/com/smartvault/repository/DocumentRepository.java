package com.smartvault.repository;

import com.smartvault.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Page<Document> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Document> findByUserIdAndCategoryIdOrderByCreatedAtDesc(Long userId, Long categoryId, Pageable pageable);

    Page<Document> findByUserIdAndFolderOrderByCreatedAtDesc(Long userId, String folder, Pageable pageable);

    Optional<Document> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT d FROM Document d WHERE d.user.id = :userId AND " +
           "(LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.tags) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Document> searchDocuments(@Param("userId") Long userId, @Param("query") String query, Pageable pageable);

    @Query("SELECT d FROM Document d WHERE d.user.id = :userId AND " +
           "d.expiryDate IS NOT NULL AND d.expiryDate BETWEEN :startDate AND :endDate " +
           "ORDER BY d.expiryDate ASC")
    List<Document> findExpiringDocuments(@Param("userId") Long userId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    @Query("SELECT d FROM Document d WHERE d.expiryDate IS NOT NULL AND " +
           "d.expiryDate BETWEEN :startDate AND :endDate ORDER BY d.expiryDate ASC")
    List<Document> findAllExpiringDocuments(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    long countByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(d.fileSize), 0) FROM Document d WHERE d.user.id = :userId")
    long sumFileSizeByUserId(@Param("userId") Long userId);

    List<Document> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Document> findByUserIdAndIsFavoriteOrderByCreatedAtDesc(Long userId, Boolean isFavorite, Pageable pageable);

    @Query("SELECT DISTINCT d.folder FROM Document d WHERE d.user.id = :userId ORDER BY d.folder")
    List<String> findDistinctFoldersByUserId(@Param("userId") Long userId);

    @Query("SELECT d.category.name, COUNT(d) FROM Document d WHERE d.user.id = :userId AND d.category IS NOT NULL GROUP BY d.category.name")
    List<Object[]> countByCategory(@Param("userId") Long userId);
}
