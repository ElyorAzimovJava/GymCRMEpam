package com.epam.service.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.dao.TrainerDAO;

import java.util.Objects;

// TODO:
//  Why is it not a bean? Let Spring do the work for you.
//  It will manage the lifecycle of the class and its dependencies, making it easier to use and test.
//  @Component
//  public class UsernameGenerator {...}
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
        // TODO:
        //  In current task users will be stored in a separate table so you can just call
        //  userRepository.existsByUsername(username.toLowerCase())
        //  and private method could be safely removed after that
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