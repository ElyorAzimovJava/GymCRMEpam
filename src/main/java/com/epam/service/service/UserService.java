package com.epam.service.service;

import com.epam.service.repository.UserRepository;
import com.epam.service.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void changePassword(String username, String newPassword) {
        log.info("Changing password for user: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean checkPassword(String username, String password) {
        log.info("Checking password for user: {}", username);
        return userRepository.findByUsername(username)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}