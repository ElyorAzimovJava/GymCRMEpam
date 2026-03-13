package com.epam.service.service;

import com.epam.service.entity.User;
import com.epam.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public boolean checkCredentials(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return user.getPassword().equals(password);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // In a real application, you would use a password encoder
        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Invalid old password");
        }

        user.setPassword(newPassword);
        userRepository.save(user);
    }
}