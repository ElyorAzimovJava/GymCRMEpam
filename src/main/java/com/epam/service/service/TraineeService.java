package com.epam.service.service;

import com.epam.service.dao.TraineeRepository;
import com.epam.service.model.Trainee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeService {

    private final TraineeRepository traineeRepository;
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
        log.info("Updating trainee with id: {}", trainee.getId());
        return traineeRepository.save(trainee);
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
}