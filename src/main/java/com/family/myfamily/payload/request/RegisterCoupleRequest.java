package com.family.myfamily.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCoupleRequest {

    private String userIin;
    private String partnerIin;
    private String city;
    private String office;
    private Boolean isUserPay;
    private UUID cardId;
}
