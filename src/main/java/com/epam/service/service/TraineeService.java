package com.epam.service.service;

import com.epam.service.repository.TraineeRepository;
import com.epam.service.repository.TrainerRepository;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.User;
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
    private final UsernameGenerator usernameGenerator;

    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        log.info("Creating trainee for user: {} {}", trainee.getUser().getFirstName(), trainee.getUser().getLastName());
        String username = usernameGenerator.generateUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName());
        String password = PasswordGenerator.generatePassword();
        trainee.getUser().setUsername(username);
        trainee.getUser().setPassword(password);
        Trainee savedTrainee = traineeRepository.save(trainee);
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