package com.family.myfamily.model.entities;

import com.family.myfamily.model.enums.Gender;
import com.family.myfamily.model.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "individuals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndividualEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(nullable = false, length = 12)
    private String iin;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @Column(name = "home_city", nullable = false)
    private String homeCity;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private String photo;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
