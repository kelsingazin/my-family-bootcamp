package com.family.myfamily.model.entities;

import com.family.myfamily.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}
