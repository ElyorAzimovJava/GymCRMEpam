package com.epam.service.service;

import com.epam.service.dao.TrainerDAO;
import com.epam.service.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainerService(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating trainer for user: {} {}", trainer.getFirstName(), trainer.getLastName());
        String username = generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        Trainer savedTrainer = trainerDAO.save(trainer);
        logger.info("Trainer created successfully with username: {}", savedTrainer.getUsername());
        return savedTrainer;
    }

    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Updating trainer with id: {}", trainer.getId());
        return trainerDAO.save(trainer);
    }

    public Optional<Trainer> selectTrainer(long id) {
        logger.info("Selecting trainer with id: {}", id);
        return trainerDAO.findById(id);
    }

    public List<Trainer> selectAllTrainers() {
        logger.info("Selecting all trainers");
        return trainerDAO.findAll();
    }

    private String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int serialNumber = 1;
        while (isUsernameTaken(username)) {
            username = baseUsername + serialNumber;
            serialNumber++;
        }
        return username;
    }

    private boolean isUsernameTaken(String username) {
        return trainerDAO.findAll().stream()
                .filter(trainer -> trainer.getUsername() != null)
                .anyMatch(trainer -> trainer.getUsername().equals(username));
    }

    private String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}