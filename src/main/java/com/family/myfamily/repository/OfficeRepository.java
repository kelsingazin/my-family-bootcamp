package com.family.myfamily.repository;

import com.family.myfamily.model.entities.OfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfficeRepository extends JpaRepository<OfficeEntity, UUID> {

    Optional<OfficeEntity> findById(UUID id);
}
