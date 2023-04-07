package com.family.myfamily.payload.response;

import com.family.myfamily.model.dto.IndividualDto;
import com.family.myfamily.model.dto.UserDto;
import com.family.myfamily.model.enums.RequestStatus;
import com.family.myfamily.model.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    private UUID id;
    private IndividualDto requestUser;
    private String city;
    private String office;
    private Date date;
    private RequestStatus status;
    private RequestType type;
}
