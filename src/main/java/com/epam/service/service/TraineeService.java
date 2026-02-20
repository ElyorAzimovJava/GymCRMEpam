package com.epam.service.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.dao.TrainerDAO;
import com.epam.service.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    private final TraineeDAO traineeDAO;
    private final UserUsername userUsername;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.traineeDAO = traineeDAO;
        this.userUsername = new UserUsername(traineeDAO, trainerDAO);
    }

    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        logger.info("Creating trainee for user: {} {}", trainee.getFirstName(), trainee.getLastName());
        String username = userUsername.generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = PasswordGenerator.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        traineeDAO.save(trainee);
        logger.info("Trainee created successfully with username: {}", trainee.getUsername());
        return trainee;
    }

    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Updating trainee with id: {}", trainee.getId());
        return traineeDAO.update(trainee);
    }

    @Transactional
    public void deleteTrainee(long id) {
        logger.info("Deleting trainee with id: {}", id);
        traineeDAO.delete(id);
    }

    @Transactional(readOnly = true)
    public Trainee selectTrainee(long id) {
        logger.info("Selecting trainee with id: {}", id);
        return traineeDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Trainee> selectAllTrainees() {
        logger.info("Selecting all trainees");
        return traineeDAO.findAll();
    }
}