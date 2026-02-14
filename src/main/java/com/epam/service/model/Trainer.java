package com.epam.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer extends User {
    private String specialization;

    public Trainer(String firstName, String lastName, String username, String password, boolean isActive, String specialization) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }
}