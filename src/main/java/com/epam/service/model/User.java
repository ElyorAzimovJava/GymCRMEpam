package com.epam.service.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
// TODO:
//  Please make sure a separate user table exists according to the task requirements.
//  It should be @Entity instead of @MappedSuperClass
//  Then you can use OneToOne relationship on Trainer and Trainee to link them to the User table
@MappedSuperclass
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // TODO: double-check the DB schema attached to the task. For the fields that are not marked as `Optional`
    //  you should add constraints to ensure integrity at the database level.
    private String firstName;
    private String lastName;
    // TODO: ensure uniqueness of username in the database
    private String username;
    private String password;
    private boolean isActive;

}