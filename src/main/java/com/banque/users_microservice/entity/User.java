package com.banque.users_microservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;


@MappedSuperclass
@Getter
@Setter
public abstract class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Date dateOfBirthday;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nationality;

    @Column(nullable = false)
    private String telephoneNumber;

}
