package com.family.myfamily.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCouple {

    private String userIin;
    private String partnerIin;
    private String city;
    private String office;
    private Boolean isUserPay;
    private String cardNumber;
}
