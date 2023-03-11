package com.family.myfamily.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    private final String type = "Baerer";
    private Long id;
    private String phoneNumber;
    private String fullName;
    private List<String> roles;

}
