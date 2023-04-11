package com.family.myfamily.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarriageCertificate {

    private UUID id;
    private String city;
    private String office;
    private String maleIin;
    private String femaleIin;
    private String maleFirstName;
    private String maleLastName;
    private String maleMiddleName;
    private String femaleFirstName;
    private String femaleLastName;
    private String femaleMiddleName;
    private String maleNationality;
    private String femaleNationality;

}
