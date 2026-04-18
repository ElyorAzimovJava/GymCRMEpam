package com.epam.service.service;

import com.epam.service.client.WorkloadClient;
import com.epam.service.dto.WorkloadRequestDto;
import com.epam.service.entity.Training;
import com.epam.service.repository.TraineeRepository;
import com.epam.service.repository.TrainerRepository;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.User;
import com.epam.service.metrics.CustomMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final UsernameGenerator usernameGenerator;
    private final CustomMetrics customMetrics;
    private final PasswordEncoder passwordEncoder;
    private final TrainingService trainingService;
    private final WorkloadClient workloadClient;

    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        log.info("Creating trainee for user: {} {}", trainee.getUser().getFirstName(), trainee.getUser().getLastName());
        String username = usernameGenerator.generateUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName());
        String password = PasswordGenerator.generatePassword();
        trainee.getUser().setUsername(username);
        trainee.getUser().setPassword(passwordEncoder.encode(password));
        Trainee savedTrainee = traineeRepository.save(trainee);
        customMetrics.incrementTraineeCreation();
        log.info("Trainee created successfully with username: {}", savedTrainee.getUser().getUsername());
        return savedTrainee;
    }

    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        log.info("Updating trainee: {}", trainee);
        return traineeRepository.save(trainee);
    }

    @Transactional(readOnly = true)
    public Trainee selectTraineeByUsername(String username) {
        log.info("Selecting trainee with username: {}", username);
        return traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found with username: " + username));
    }

    @Transactional
    public void deleteTrainee(long id) {
        log.info("Deleting trainee with id: {}", id);
        Trainee trainee = selectTrainee(id);
        deleteTraineeByUsername(trainee.getUser().getUsername());
    }

    @Transactional(readOnly = true)
    public Trainee selectTrainee(long id) {
        log.info("Selecting trainee with id: {}", id);
        return traineeRepository.findById(id).orElseThrow(() -> new RuntimeException("Trainee not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Trainee> selectAllTrainees() {
        log.info("Selecting all trainees");
        return traineeRepository.findAll();
    }

    @Transactional
    public void changeTraineePassword(String username, String oldPassword, String newPassword) {
        log.info("Changing password for trainee: {}", username);
        userService.changePassword(username, oldPassword, newPassword);
    }

    @Transactional
    public void activateTrainee(String username) {
        log.info("Activating trainee: {}", username);
        Trainee trainee = selectTraineeByUsername(username);
        trainee.getUser().setActive(true);
        traineeRepository.save(trainee);
    }

    @Transactional
    public void deactivateTrainee(String username) {
        log.info("Deactivating trainee: {}", username);
        Trainee trainee = selectTraineeByUsername(username);
        trainee.getUser().setActive(false);
        traineeRepository.save(trainee);
    }

    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        Trainee trainee = selectTraineeByUsername(username);
        List<Training> trainings = trainingService.getTraineeTrainings(username, null, null, null, null);
        for (Training training : trainings) {
            WorkloadRequestDto workloadRequest = WorkloadRequestDto.builder()
                    .trainerUsername(training.getTrainer().getUser().getUsername())
                    .trainerFirstName(training.getTrainer().getUser().getFirstName())
                    .trainerLastName(training.getTrainer().getUser().getLastName())
                    .traineeUsername(trainee.getUser().getUsername())
                    .traineeFirstName(trainee.getUser().getFirstName())
                    .traineeLastName(trainee.getUser().getLastName())
                    .isActive(training.getTrainer().getUser().isActive())
                    .trainingDate(training.getTrainingDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .trainingDuration(Duration.ofMinutes(training.getTrainingDuration().toMinutes()))
                    .trainingType(training.getTrainingType())
                    .actionType("DELETE")
                    .build();
            workloadClient.handleWorkload(workloadRequest);
        }
        traineeRepository.delete(trainee);
    }

    @Transactional
    public void updateTraineeTrainers(String username, List<String> trainerUsernames) {
        log.info("Updating trainers for trainee: {}", username);
        Trainee trainee = selectTraineeByUsername(username);
        List<Trainer> trainers = trainerUsernames.stream()
                .map(trainerUsername -> trainerRepository.findByUserUsername(trainerUsername)
                        .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + trainerUsername)))
                .collect(Collectors.toList());
        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);
    }
}