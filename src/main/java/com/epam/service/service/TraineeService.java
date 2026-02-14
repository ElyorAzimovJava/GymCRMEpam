package com.epam.service.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private final TraineeDAO traineeDAO;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    public Trainee createTrainee(Trainee trainee) {
        logger.info("Creating trainee for user: {} {}", trainee.getFirstName(), trainee.getLastName());
        String username = generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        Trainee savedTrainee = traineeDAO.save(trainee);
        logger.info("Trainee created successfully with username: {}", savedTrainee.getUsername());
        return savedTrainee;
    }

    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Updating trainee with id: {}", trainee.getId());
        return traineeDAO.save(trainee);
    }

    public void deleteTrainee(long id) {
        logger.info("Deleting trainee with id: {}", id);
        traineeDAO.delete(id);
    }

    public Optional<Trainee> selectTrainee(long id) {
        logger.info("Selecting trainee with id: {}", id);
        return traineeDAO.findById(id);
    }

    public List<Trainee> selectAllTrainees() {
        logger.info("Selecting all trainees");
        return traineeDAO.findAll();
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
        return traineeDAO.findAll().stream()
                .filter(trainee -> trainee.getUsername() != null)
                .anyMatch(trainee -> trainee.getUsername().equals(username));
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