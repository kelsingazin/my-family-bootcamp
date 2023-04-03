package com.family.myfamily.model.dto;

import com.family.myfamily.model.enums.RequestType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
public class GovernmentRequestDto {

    private UUID id;
    private UserDto requestUser;
    private String city;
    private String office;
    private Date date;
    private String status;
    private RequestType type;

}
