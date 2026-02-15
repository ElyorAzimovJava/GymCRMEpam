package com.epam.service.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.dao.TrainerDAO;
import com.epam.service.model.Trainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerService {

    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    private final TrainerDAO trainerDAO;
    private final UserUsername userUsername;

    @Autowired
    public TrainerService(TrainerDAO trainerDAO, TraineeDAO traineeDAO) {
        this.trainerDAO = trainerDAO;
        this.userUsername = new UserUsername(traineeDAO, trainerDAO);
    }

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating trainer for user: {} {}", trainer.getFirstName(), trainer.getLastName());
        String username = userUsername.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = PasswordGenerator.generatePassword();
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


}