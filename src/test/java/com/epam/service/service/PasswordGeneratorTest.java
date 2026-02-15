package com.epam.service.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PasswordGeneratorTest {

    @Test
    public void testGeneratePassword() {
        String password = PasswordGenerator.generatePassword();
        assertNotNull(password);
        assertEquals(10, password.length());
    }
}