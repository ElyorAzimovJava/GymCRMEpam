package com.epam.service.service;

import com.epam.service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameGeneratorTest {

    @InjectMocks
    private UsernameGenerator usernameGenerator;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGenerateUsername() {
        when(userRepository.existsByUsername("john.doe")).thenReturn(false);

        String username = usernameGenerator.generateUsername("John", "Doe");
        assertEquals("John.Doe", username);
    }

    @Test
    void testGenerateUsernameWhenTaken() {
        when(userRepository.existsByUsername("john.doe")).thenReturn(true);
        when(userRepository.existsByUsername("john.doe1")).thenReturn(false);

        String username = usernameGenerator.generateUsername("John", "Doe");
        assertEquals("John.Doe1", username);
    }

    @Test
    void testGenerateUsernameWhenTakenMultipleTimes() {
        when(userRepository.existsByUsername("john.doe")).thenReturn(true);
        when(userRepository.existsByUsername("john.doe1")).thenReturn(true);
        when(userRepository.existsByUsername("john.doe2")).thenReturn(true);
        when(userRepository.existsByUsername("john.doe3")).thenReturn(false);

        String username = usernameGenerator.generateUsername("John", "Doe");
        assertEquals("John.Doe3", username);
    }
}