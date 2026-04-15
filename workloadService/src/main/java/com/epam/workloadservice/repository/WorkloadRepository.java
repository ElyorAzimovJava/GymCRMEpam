package com.epam.workloadservice.repository;

import com.epam.workloadservice.entity.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkloadRepository extends JpaRepository<TrainerWorkload, Long> {
    Optional<TrainerWorkload> findByTrainerUsername(String trainerUsername);
    Optional<TrainerWorkload> findByTrainerUsernameAndTrainingDateAndTrainingDuration(String trainerUsername, java.time.LocalDate trainingDate, int trainingDuration);
}