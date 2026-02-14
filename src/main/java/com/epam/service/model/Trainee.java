package com.epam.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainee extends User {
    private Date dateOfBirth;
    private String address;

    public Trainee(String firstName, String lastName, String username, String password, boolean isActive, Date dateOfBirth, String address) {
        super(firstName, lastName, username, password, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}