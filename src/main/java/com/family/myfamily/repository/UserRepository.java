package com.family.myfamily.repository;

import com.family.myfamily.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByPhoneNumber(String phoneNumber);

    UserEntity findById(UUID id);

    Boolean existsUserByPhoneNumber(String number);
}
