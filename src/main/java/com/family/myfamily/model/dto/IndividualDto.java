package com.family.myfamily.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class IndividualDto {

    public UUID id;
    public String firstName;
    public String lastName;
    public String middleName;
    public String iin;
    public Date birthDate;
    public String homeCity;
    public String nationality;
    public String photo;
}
