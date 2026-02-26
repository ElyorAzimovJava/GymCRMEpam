package com.epam.service.service;

import com.epam.service.dao.TrainingRepository;
import com.epam.service.model.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    @Transactional
    public Training createTraining(Training training) {
        log.info("Creating training: {}", training.getTrainingName());
        return trainingRepository.save(training);
    }

    @Transactional(readOnly = true)
    public Training selectTraining(long id) {
        log.info("Selecting training with id: {}", id);
        return trainingRepository.findById(id).orElseThrow(() -> new RuntimeException("Training not found with id: " + id));
    }

    @Transactional
    public void deleteTraining(long id) {
        log.info("Deleting training with id: {}", id);
        trainingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Training> selectAllTrainings() {
        log.info("Selecting all trainings");
        return trainingRepository.findAll();
    }
}