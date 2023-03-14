package com.family.myfamily.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private String password;
    private String role;

}
