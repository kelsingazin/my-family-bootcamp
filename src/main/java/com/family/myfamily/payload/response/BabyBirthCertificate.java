package com.family.myfamily.payload.response;

import com.family.myfamily.model.dto.Parent;
import com.family.myfamily.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BabyBirthCertificate {

    private UUID id;
    private String city;
    private String office;
    private String country;
    private Parent father;
    private Parent mother;
//    private String maleFirstName;
//    private String maleLastName;
//    private String maleMiddleName;
//    private String femaleFirstName;
//    private String femaleLastName;
//    private String femaleMiddleName;
//    private String maleNationality;
//    private String femaleNationality;
    private String babyFirstName;
    private String babyLastName;
    private String babyMiddleName;
    private Gender babyGender;

}
