package com.epam.service.dao;

import com.epam.service.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByTraineeUsername(String username);
    List<Training> findByTrainerUsername(String username);
}