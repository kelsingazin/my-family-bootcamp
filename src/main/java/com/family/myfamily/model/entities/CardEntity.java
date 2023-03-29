package com.family.myfamily.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "credit_cards")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "alias")
    private String alias;

    @Column(name = "number")
    private String number;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "security_code")
    private Integer securityCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @ManyToOne
    @JsonBackReference
    private UserEntity user;
}
