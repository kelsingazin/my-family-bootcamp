package com.family.myfamily.model.dto;

import com.family.myfamily.model.entities.OfficeEntity;
import com.family.myfamily.model.enums.DocumentType;
import com.family.myfamily.model.enums.DriverLicenseCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CityDto {

    public UUID id;
    public String name;
    public List<OfficeDto> officeList;
}
