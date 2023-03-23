package com.family.myfamily.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDto {

    public UUID id;
    public String fullName;
    public String phoneNumber;
    private String password;
    private String role;
}
