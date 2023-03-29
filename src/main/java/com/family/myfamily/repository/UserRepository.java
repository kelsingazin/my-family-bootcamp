package com.family.myfamily.repository;

import com.family.myfamily.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findById(UUID id);
}
