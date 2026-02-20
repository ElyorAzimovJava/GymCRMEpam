package com.epam.service.service;

import com.epam.service.dao.TrainingDAO;
import com.epam.service.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingService(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Transactional
    public Training createTraining(Training training) {
        logger.info("Creating training: {}", training.getTrainingName());
        trainingDAO.save(training);
        return training;
    }

    @Transactional(readOnly = true)
    public Training selectTraining(long id) {
        logger.info("Selecting training with id: {}", id);
        return trainingDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Training> selectAllTrainings() {
        logger.info("Selecting all trainings");
        return trainingDAO.findAll();
    }
}