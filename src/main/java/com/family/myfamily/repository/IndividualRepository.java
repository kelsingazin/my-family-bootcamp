package com.family.myfamily.repository;

import com.family.myfamily.model.entities.IndividualEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IndividualRepository extends JpaRepository<IndividualEntity, Long> {

    IndividualEntity findById(UUID id);

    IndividualEntity findByIin(String iin);
}