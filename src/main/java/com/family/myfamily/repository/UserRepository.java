package com.family.myfamily.repository;

import com.family.myfamily.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByPhoneNumber(String phoneNumber);
    Boolean existsUserByPhoneNumber(String number);

}
