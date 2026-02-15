package com.epam.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.atomic.AtomicLong;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {
    private static final AtomicLong idCounter = new AtomicLong();

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;

}