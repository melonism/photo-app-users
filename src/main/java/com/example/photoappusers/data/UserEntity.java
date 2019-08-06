package com.example.photoappusers.data;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -1208324112468589686L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false, length=50)
    private String firstName;

    @Column(nullable=false, length=50)
    private String lastName;

    @Column(nullable=false, length=120, unique = true)
    private String email;

    @Column(nullable=false, length=50, unique = true)
    private String userId;

    @Column(nullable=false)
    private String encryptedPassword;
}
