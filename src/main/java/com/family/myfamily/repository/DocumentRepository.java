package com.family.myfamily.repository;

import com.family.myfamily.model.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {

    List<DocumentEntity> findAllByUser_Id(UUID userId);

    Optional<DocumentEntity> findByPassportSeries(String series);

    Optional<DocumentEntity> findByLicenseNumber(String licenseNumber);
}
