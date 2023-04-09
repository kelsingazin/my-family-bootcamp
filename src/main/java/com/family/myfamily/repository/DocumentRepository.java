package com.family.myfamily.repository;

import com.family.myfamily.model.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {

    List<DocumentEntity> findAllByUser_IdAndDeletedIsFalse(UUID userId);

    Optional<DocumentEntity> findByPassportSeriesAndDeletedIsFalse(String series);

    Optional<DocumentEntity> findByLicenseNumberAndDeletedIsFalse(String licenseNumber);

    Optional<DocumentEntity> findByIdAndDeletedIsFalse(UUID uuid);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE DocumentEntity d SET d.deleted = :isDeleted WHERE d.id = :documentId")
    void deleteDocumentEntity(@Param("isDeleted") boolean isDeleted,
                              @Param("documentId") UUID documentId);
}
