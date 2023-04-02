package com.family.myfamily.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CardDto {

    public UUID id;
    public String number;
    public String expirationDate;
    public String cardHolderName;
    public Integer securityCode;
    public Double balance;
    public UserDto user;
}
