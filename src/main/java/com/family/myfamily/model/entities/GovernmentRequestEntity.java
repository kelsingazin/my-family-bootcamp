package com.family.myfamily.model.entities;

import com.family.myfamily.model.enums.RequestStatus;
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

@Table(name = "requests")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GovernmentRequestEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "request_user_id")
    @NotNull(message = "individual is required")
    private UserEntity requestUser;

    @ManyToOne
    @JoinColumn(name = "response_user_id")
    private UserEntity responseUser;

    @Column(name = "city")
    private String city;

    @Column(name = "office")
    private String office;

    @Column(name = "is_partner_paid")
    private Boolean isPartnerPaid;

    @Column(name = "date")
    private Date date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private RequestType type;

    @ManyToOne
    @JoinColumn(name = "mother_individual_id")
    private IndividualEntity mother;

    @ManyToOne
    @JoinColumn(name = "father_individual_id")
    private IndividualEntity father;

    @Column(name = "country")
    private String country;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;
}
