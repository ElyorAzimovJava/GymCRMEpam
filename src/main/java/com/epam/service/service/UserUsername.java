package com.epam.service.service;

import com.epam.service.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUsername {
    private final UserRepository userRepository;

    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int serialNumber = 1;
        while (userRepository.existsByUsername(username.toLowerCase())) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }
        return username;
    }
}