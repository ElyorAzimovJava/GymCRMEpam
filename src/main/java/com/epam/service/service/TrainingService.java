package com.epam.service.service;

import com.epam.service.repository.TrainingRepository;
import com.epam.service.entity.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


import com.epam.service.entity.TrainingType;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    @Transactional
    public Training createTraining(Training training) {
        log.info("Creating training: {}", training);
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
    public List<Training> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) {
        log.info("Getting trainings for trainee: {}", username);
        return trainingRepository.findTrainerTrainingsByCriteria(username,fromDate,toDate,trainerName,trainingType);
/*        return trainings.stream()
                .filter(t -> fromDate == null || t.getTrainingDate().after(fromDate))
                .filter(t -> toDate == null || t.getTrainingDate().before(toDate))
                .filter(t -> trainerName == null || t.getTrainer().getFirstName().equals(trainerName))
                .filter(t -> trainingType == null || t.getTrainingType().equals(trainingType))
                .collect(Collectors.toList());*/
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName) {
        log.info("Getting trainings for trainer: {}", username);
        return trainingRepository.findTraineeTrainingsByCriteria(username, fromDate, toDate, traineeName);
    }

    @Transactional(readOnly = true)
    public List<Training> selectAllTrainings() {
        log.info("Selecting all trainings");
        return trainingRepository.findAll();
    }
}