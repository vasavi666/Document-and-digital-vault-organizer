package com.smartvault.repository;

import com.smartvault.model.OcrData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OcrDataRepository extends JpaRepository<OcrData, Long> {
    Optional<OcrData> findByDocumentId(Long documentId);

    @Query("SELECT o.document.id FROM OcrData o WHERE o.status = 'COMPLETED' AND " +
           "LOWER(o.extractedText) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Long> findDocumentIdsByExtractedTextContaining(@Param("query") String query);
}
