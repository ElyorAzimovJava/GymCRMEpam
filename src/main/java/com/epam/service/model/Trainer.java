package com.epam.service.model;

public class Trainer extends User {
    private String specialization;

    public Trainer(String firstName, String lastName, String username, String password, boolean isActive, String specialization) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}