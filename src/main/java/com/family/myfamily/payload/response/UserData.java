package com.family.myfamily.payload.response;

import com.family.myfamily.model.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {

    private String firstName;
    private String lastName;
    private MaritalStatus status;
    private String iin;
    private String cardNumber;
    private Double balance;
    private String email;
}
