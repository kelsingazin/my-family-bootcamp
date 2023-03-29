package com.family.myfamily.repository;

import com.family.myfamily.model.entities.GovernmentRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GovernmentRequestRepository extends JpaRepository<GovernmentRequestEntity, UUID> {
}
