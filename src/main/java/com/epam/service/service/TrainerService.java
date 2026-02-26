package com.epam.service.service;

import com.epam.service.dao.TrainerRepository;
import com.epam.service.model.Trainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserUsername userUsername;

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

    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        log.info("Updating trainer with id: {}", trainer.getId());
        return trainerRepository.save(trainer);
    }

    @Transactional
    public void deleteTrainer(long id) {
        log.info("Deleting trainer with id: {}", id);
        trainerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Trainer selectTrainer(long id) {
        log.info("Selecting trainer with id: {}", id);
        return trainerRepository.findById(id).orElseThrow(() -> new RuntimeException("Trainer not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Trainer> selectAllTrainers() {
        log.info("Selecting all trainers");
        return trainerRepository.findAll();
    }
}