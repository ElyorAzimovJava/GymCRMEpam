package com.epam.service.service;

import com.epam.service.repository.TrainerRepository;
import com.epam.service.entity.Trainee;
import com.epam.service.metrics.CustomMetrics;
import com.epam.service.entity.Trainer;
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
    private final UsernameGenerator usernameGenerator;
    private final TraineeService traineeService;
    private final CustomMetrics customMetrics;

    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        log.info("Creating trainer for user: {} {}", trainer.getUser().getFirstName(), trainer.getUser().getLastName());
        String username = usernameGenerator.generateUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName());
        String password = PasswordGenerator.generatePassword();
        trainer.getUser().setUsername(username);
        trainer.getUser().setPassword(password);
        Trainer savedTrainer = trainerRepository.save(trainer);
        customMetrics.incrementTrainerCreation();
        log.info("Trainer created successfully with username: {}", savedTrainer.getUser().getUsername());
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
        return trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found with username: " + username));
    }

    @Transactional
    public void deleteTrainer(long id) {
        log.info("Deleting trainer with id: {}", id);
        trainerRepository.deleteById(id);
    }

    @Transactional
    public void changeTrainerPassword(String username, String oldPassword, String newPassword) {
        log.info("Changing password for trainer: {}", username);
        userService.changePassword(username, oldPassword, newPassword);
    }

    @Transactional
    public void activateTrainer(String username) {
        log.info("Activating trainer: {}", username);
        Trainer trainer = selectTrainerByUsername(username);
        trainer.getUser().setActive(true);
        trainerRepository.save(trainer);
    }

    @Transactional
    public void deactivateTrainer(String username) {
        log.info("Deactivating trainer: {}", username);
        Trainer trainer = selectTrainerByUsername(username);
        trainer.getUser().setActive(false);
        trainerRepository.save(trainer);
    }

    @Transactional(readOnly = true)
    public List<Trainer> getUnassignedTrainers(String traineeUsername) {
        log.info("Getting unassigned trainers for trainee: {}", traineeUsername);
        Trainee trainee = traineeService.selectTraineeByUsername(traineeUsername);
        // TODO:
        //  [Optional]
        //  Could also be done via db level with trainerRepository.findAllByTraineesNotContaining(Trainee trainee)
        List<Trainer> allTrainers = trainerRepository.findAll();
        return allTrainers.stream()
                .filter(trainer -> !trainee.getTrainers().contains(trainer))
                .collect(Collectors.toList());
    }
}