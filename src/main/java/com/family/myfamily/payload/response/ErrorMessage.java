package com.family.myfamily.payload.response;

import com.family.myfamily.payload.codes.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {

    public String message;
    public ErrorCode errorCode;
}
