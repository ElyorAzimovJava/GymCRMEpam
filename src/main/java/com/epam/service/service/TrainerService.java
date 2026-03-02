package com.epam.service.service;

import com.epam.service.dao.TrainerRepository;
import com.epam.service.model.Trainee;
import com.epam.service.model.Trainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final UserUsername userUsername;
    private final TraineeService traineeService;

    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        log.info("Creating trainer for user: {} {}", trainer.getFirstName(), trainer.getLastName());
        String username = userUsername.generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = PasswordGenerator.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        Trainer savedTrainer = trainerRepository.save(trainer);
        log.info("Trainer created successfully with username: {}", savedTrainer.getUsername());
        return savedTrainer;
    }
    @Transactional(readOnly = true)
    public List<Trainer> selectAllTrainers() {
        log.info("Selecting all trainers");
        return trainerRepository.findAll();
    }

    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        log.info("Updating trainer: {}", trainer);
        return trainerRepository.save(trainer);
    }

    @Transactional(readOnly = true)
    public Trainer selectTrainer(long id) {
        log.info("Selecting trainer with id: {}", id);
        return trainerRepository.findById(id).orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Trainer selectTrainerByUsername(String username) {
        log.info("Selecting trainer with username: {}", username);
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + username));
    }

    @Transactional
    public void deleteTrainer(long id) {
        log.info("Deleting trainer with id: {}", id);
        trainerRepository.deleteById(id);
    }

    @Transactional
    public void changeTrainerPassword(String username, String newPassword) {
        log.info("Changing password for trainer: {}", username);
        userService.changePassword(username, newPassword);
    }

    @Transactional
    public void activateTrainer(String username) {
        log.info("Activating trainer: {}", username);
        Trainer trainer = selectTrainerByUsername(username);
        trainer.setActive(true);
        trainerRepository.save(trainer);
    }

    @Transactional
    public void deactivateTrainer(String username) {
        log.info("Deactivating trainer: {}", username);
        Trainer trainer = selectTrainerByUsername(username);
        trainer.setActive(false);
        trainerRepository.save(trainer);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.info("Getting unassigned trainers for trainee: {}", traineeUsername);
        Trainee trainee = traineeService.selectTraineeByUsername(traineeUsername);
        List<Trainer> allTrainers = trainerRepository.findAll();
        return allTrainers.stream()
                .filter(trainer -> !trainee.getTrainers().contains(trainer))
                .collect(Collectors.toList());
    }
}