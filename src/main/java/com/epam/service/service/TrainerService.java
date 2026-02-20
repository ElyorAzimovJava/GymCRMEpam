package com.epam.service.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.dao.TrainerDAO;
import com.epam.service.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating trainer for user: {} {}", trainer.getFirstName(), trainer.getLastName());
        String username = userUsername.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = PasswordGenerator.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        trainerDAO.save(trainer);
        logger.info("Trainer created successfully with username: {}", trainer.getUsername());
        return trainer;
    }

    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Updating trainer with id: {}", trainer.getId());
        return trainerDAO.update(trainer);
    }

    @Transactional(readOnly = true)
    public Trainer selectTrainer(long id) {
        logger.info("Selecting trainer with id: {}", id);
        return trainerDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Trainer> selectAllTrainers() {
        logger.info("Selecting all trainers");
        return trainerDAO.findAll();
    }
}