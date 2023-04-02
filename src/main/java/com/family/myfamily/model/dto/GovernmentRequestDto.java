package com.family.myfamily.model.dto;

import com.family.myfamily.model.entities.UserEntity;
import com.family.myfamily.model.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
public class GovernmentRequestDto {

    private UUID id;
    private UserDto requestUser;
    private String city;
    private String office;
    private Date date;
    private String status;
    private RequestType type;

}
