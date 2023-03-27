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
    public Date issueDate;
    public Date expirationDate;
    public String passportSeries;

    public String licenseNumber;
    public String issuingAuthority;
    public DriverLicenseCategory driverLicenseCategory;

    public UserDto user;
    public IndividualDto individual;
}
