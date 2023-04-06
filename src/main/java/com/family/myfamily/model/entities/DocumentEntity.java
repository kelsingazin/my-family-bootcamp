package com.family.myfamily.model.entities;

import com.family.myfamily.model.enums.DocumentType;
import com.family.myfamily.model.enums.DriverLicenseCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(nullable = false, name = "issue_date")
    private Date issueDate;

    @Column(nullable = false, name = "expiration_date")
    private Date expirationDate;

    //Only for passport
    @Column(name = "series", unique = true)
    private String passportSeries;

    //Only for driver license
    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    //Only for driver license
    @Column(name = "issuing_authority")
    private String issuingAuthority;

    //Only for driver license
    @Column(name = "driver_license_category")
    @Enumerated(EnumType.STRING)
    private DriverLicenseCategory driverLicenseCategory;

    @Column(name = "is_deleted")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "user is required")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "individual_id")
    @NotNull(message = "individual is required")
    private IndividualEntity individual;

    @PrePersist
    void setActive() {
        this.deleted = Boolean.FALSE;
    }
}
