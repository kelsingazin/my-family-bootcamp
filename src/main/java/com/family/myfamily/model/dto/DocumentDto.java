package com.family.myfamily.model.dto;

import com.family.myfamily.model.enums.DocumentType;
import com.family.myfamily.model.enums.DriverLicenseCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DocumentDto {

    public UUID id;
    public DocumentType documentType;
    //Only for driver license
    public String licenseNumber;
    //Only for passport
    public String passportSeries;
    public String name;
    public String surname;
    public String middleName;
    public String iin;
    public Date birthDate;
    public String homeCity;
    public String nationality;
    public Date issueDate;
    public Date expirationDate;
    public String photo;
    //Only for driver license
    public String issuingAuthority;
    //Only for driver license
    public DriverLicenseCategory driverLicenseCategory;
    public UserDto user;
}
