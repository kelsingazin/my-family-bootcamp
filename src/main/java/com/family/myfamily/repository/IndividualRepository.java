package com.family.myfamily.repository;

import com.family.myfamily.model.entities.IndividualEntity;
import com.family.myfamily.model.enums.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndividualRepository extends JpaRepository<IndividualEntity, UUID> {

    Optional<IndividualEntity> findById(UUID id);

    IndividualEntity findByIin(String iin);

    IndividualEntity findByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query(value = "UPDATE IndividualEntity i SET i.maritalStatus = :maritalStatus WHERE i.phoneNumber = :phoneNumber")
    void updateMarriageStatus(@Param("phoneNumber") String phoneNumber,
                              @Param("maritalStatus") MaritalStatus maritalStatus);
}
