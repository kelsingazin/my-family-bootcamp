package com.family.myfamily.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterBabyRequest {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String city;
    private String office;
    private String country;
    private Date birthDate;
    private String motherIin;
    private String fatherIin;
    private String fatherFirstName;
    private String fatherLatsName;
    private String motherFirstName;
    private String motherLastName;
    private UUID cardId;
}
