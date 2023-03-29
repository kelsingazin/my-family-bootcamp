package com.family.myfamily.repository;

import com.family.myfamily.model.entities.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {

    Optional<CardEntity> findByIdAndDeletedAtNull(UUID uuid);

    List<CardEntity> findAllByUser_IdAndDeletedAtNull(UUID userId);

    void delete(CardEntity entity);
}
