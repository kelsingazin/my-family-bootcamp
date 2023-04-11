package com.family.myfamily.repository;

import com.family.myfamily.model.entities.GovernmentRequestEntity;
import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.model.enums.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface GovernmentRequestRepository extends JpaRepository<GovernmentRequestEntity, UUID> {

    List<GovernmentRequestEntity> findAllByRequestUser(UserEntity requestUser);

    List<GovernmentRequestEntity> findAllByResponseUser(UserEntity responseUser);

    GovernmentRequestEntity findByRequestUserAndResponseUser(UserEntity requestUser, UserEntity responseUser);

    GovernmentRequestEntity findByBirthDate(Date birthDate);

    GovernmentRequestEntity findByRequestUserAndType(UserEntity requestUser, RequestType requestType);
    GovernmentRequestEntity findByResponseUserAndType(UserEntity requestUser, RequestType requestType);
}
