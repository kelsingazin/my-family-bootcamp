package com.family.myfamily.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
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

    @Column(name = "is_deleted")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @PrePersist
    void setActive() {
        this.deleted = Boolean.FALSE;
    }
}
