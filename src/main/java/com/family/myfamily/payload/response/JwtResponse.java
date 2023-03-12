package com.family.myfamily.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    private final String type = "Baerer";
    private UUID id;
    private String phoneNumber;
    private String fullName;
    private String roles;

}
