package com.epam.service.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.dao.TrainerDAO;

import java.util.Objects;

public class UserUsername {
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    public UserUsername(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    public String generateUsername(String firstName, String lastName) {
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
                .anyMatch(trainee -> Objects.equals(trainee.getUsername(), username)) ||
                trainerDAO.findAll().stream()
                        .anyMatch(trainer -> Objects.equals(trainer.getUsername(), username));
    }
}