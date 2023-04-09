package com.family.myfamily.repository;

import com.family.myfamily.model.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, UUID> {

    Optional<CityEntity> findById(UUID id);
}
