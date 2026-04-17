package com.epam.workloadservice.repository;

import com.epam.workloadservice.entity.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkloadRepository extends JpaRepository<TrainerWorkload, Long> {
    Optional<TrainerWorkload> findByTrainerUsernameAndYearAndMonth(String trainerUsername, int year, int month);
}