package com.epam.service.service;

import com.epam.service.dao.TraineeRepository;
import com.epam.service.dao.TrainerRepository;
import com.epam.service.model.Trainee;
import com.epam.service.model.Trainer;
import com.epam.service.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final UserUsername userUsername;

    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        log.info("Creating trainee for user: {} {}", trainee.getFirstName(), trainee.getLastName());
        String username = userUsername.generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = PasswordGenerator.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        Trainee savedTrainee = traineeRepository.save(trainee);
        log.info("Trainee created successfully with username: {}", savedTrainee.getUsername());
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
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found with username: " + username));
    }

    @Transactional
    public void deleteTrainee(long id) {
        log.info("Deleting trainee with id: {}", id);
        traineeRepository.deleteById(id);
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
    public void changeTraineePassword(String username, String newPassword) {
        log.info("Changing password for trainee: {}", username);
        userService.changePassword(username, newPassword);
    }

    @Transactional
    public void activateTrainee(String username) {
        log.info("Activating trainee: {}", username);
        Trainee trainee = selectTraineeByUsername(username);
        trainee.setActive(true);
        traineeRepository.save(trainee);
    }

    @Transactional
    public void deactivateTrainee(String username) {
        log.info("Deactivating trainee: {}", username);
        Trainee trainee = selectTraineeByUsername(username);
        trainee.setActive(false);
        traineeRepository.save(trainee);
    }

    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        Trainee trainee = selectTraineeByUsername(username);
        traineeRepository.delete(trainee);
    }

    @Transactional
    public void updateTraineeTrainers(String username, List<String> trainerUsernames) {
        log.info("Updating trainers for trainee: {}", username);
        Trainee trainee = selectTraineeByUsername(username);
        List<Trainer> trainers = trainerUsernames.stream()
                .map(trainerUsername -> trainerRepository.findByUsername(trainerUsername)
                        .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + trainerUsername)))
                .collect(Collectors.toList());
        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);
    }
}