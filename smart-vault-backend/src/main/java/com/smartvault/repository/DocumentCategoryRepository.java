package com.smartvault.repository;

import com.smartvault.model.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long> {
    List<DocumentCategory> findByIsSystemOrderByNameAsc(boolean isSystem);
    List<DocumentCategory> findAllByOrderByNameAsc();
    boolean existsByName(String name);
}
