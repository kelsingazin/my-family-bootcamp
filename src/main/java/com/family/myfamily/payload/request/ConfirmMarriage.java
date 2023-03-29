package com.family.myfamily.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmMarriage {

    private UUID userId;
    private UUID governmentRequestId;
    private Boolean confirm;
}
