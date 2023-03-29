package com.family.myfamily.repository;

import com.family.myfamily.model.entities.GovernmentRequestEntity;
import com.family.myfamily.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Repository
public interface GovernmentRequestRepository extends JpaRepository<GovernmentRequestEntity, UUID> {

    List<GovernmentRequestEntity> findAllByRequestUser(UserEntity requestUser);

}
