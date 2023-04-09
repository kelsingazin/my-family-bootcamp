package com.family.myfamily.repository;

import com.family.myfamily.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findById(UUID id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE UserEntity u SET u.email = :email WHERE u.id = :userId")
    void updateUserEmail(@Param("userId") UUID userId,
                         @Param("email") String email);
}
