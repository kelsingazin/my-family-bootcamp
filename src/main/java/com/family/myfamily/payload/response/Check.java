package com.family.myfamily.payload.response;

import com.family.myfamily.model.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Check {

    private UUID requestId;
    private RequestType type;
    private Double sum;
    private LocalDateTime date;
}
