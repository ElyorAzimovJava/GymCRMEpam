package com.epam.service.service;

import com.epam.service.client.WorkloadClient;
import com.epam.service.dto.WorkloadRequestDto;
import com.epam.service.repository.TrainingRepository;
import com.epam.service.entity.Training;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


import com.epam.service.entity.TrainingType;

import java.time.Duration;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final WorkloadClient workloadClient;

    @Transactional
    public Training createTraining(Training training) {
        log.info("Creating training: {}", training);
        Training savedTraining = trainingRepository.save(training);
        WorkloadRequestDto workloadRequest = WorkloadRequestDto.builder()
                .trainerUsername(training.getTrainer().getUser().getUsername())
                .trainerFirstName(training.getTrainer().getUser().getFirstName())
                .trainerLastName(training.getTrainer().getUser().getLastName())
                .traineeUsername(training.getTrainee().getUser().getUsername())
                .traineeFirstName(training.getTrainee().getUser().getFirstName())
                .traineeLastName(training.getTrainee().getUser().getLastName())
                .isActive(training.getTrainer().getUser().isActive())
                .trainingDate(training.getTrainingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .trainingDuration(training.getTrainingDuration())
                .trainingType(training.getTrainingType())
                .actionType("ADD")
                .build();
        workloadClient.handleWorkload(workloadRequest);
        return savedTraining;
    }

    @Transactional(readOnly = true)
    public Training selectTraining(long id) {
        log.info("Selecting training with id: {}", id);
        return trainingRepository.findById(id).orElseThrow(() -> new RuntimeException("Training not found with id: " + id));
    }

    @Transactional
    public void deleteTraining(long id) {
        log.info("Deleting training with id: {}", id);
        Training training = selectTraining(id);
        WorkloadRequestDto workloadRequest = WorkloadRequestDto.builder()
                .trainerUsername(training.getTrainer().getUser().getUsername())
                .trainerFirstName(training.getTrainer().getUser().getFirstName())
                .trainerLastName(training.getTrainer().getUser().getLastName())
                .traineeUsername(training.getTrainee().getUser().getUsername())
                .traineeFirstName(training.getTrainee().getUser().getFirstName())
                .traineeLastName(training.getTrainee().getUser().getLastName())
                .isActive(training.getTrainer().getUser().isActive())
                .trainingDate(training.getTrainingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .trainingDuration(training.getTrainingDuration())
                .trainingType(training.getTrainingType())
                .actionType("DELETE")
                .build();
        workloadClient.handleWorkload(workloadRequest);
        trainingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) {
        log.info("Getting trainings for trainee: {}", username);
        return trainingRepository.findTraineeTrainingsByCriteria(username, fromDate, toDate, trainerName, trainingType);
/*        return trainings.stream()
                .filter(t -> fromDate == null || t.getTrainingDate().after(fromDate))
                .filter(t -> toDate == null || t.getTrainingDate().before(toDate))
                .filter(t -> trainerName == null || t.getTrainer().getFirstName().equals(trainerName))
                .filter(t -> trainingType == null || t.getTrainingType().equals(trainingType))
                .collect(Collectors.toList());*/
    }

    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName,TrainingType trainingType) {
        log.info("Getting trainings for trainer: {}", username);
        return trainingRepository.findTrainerTrainingsByCriteria(username, fromDate, toDate, traineeName, trainingType);
    }

    @Transactional(readOnly = true)
    public List<Training> selectAllTrainings() {
        log.info("Selecting all trainings");
        return trainingRepository.findAll();
    }
}