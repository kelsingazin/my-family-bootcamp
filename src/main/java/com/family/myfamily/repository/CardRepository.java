package com.family.myfamily.repository;

import com.family.myfamily.model.entities.CardEntity;
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
public interface CardRepository extends JpaRepository<CardEntity, UUID> {

    Optional<CardEntity> findByIdAndDeletedIsFalse(UUID uuid);

    List<CardEntity> findAllByUser_IdAndDeletedIsFalse(UUID userId);

    void delete(CardEntity entity);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE CardEntity c SET c.balance = ?1 WHERE c.id = ?2")
    void updateCardBalanceStatus(@Param("balance") Double balance,
                                 @Param("id") UUID uuid);
}
