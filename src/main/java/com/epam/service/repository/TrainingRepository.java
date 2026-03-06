package com.epam.service.repository;

import com.epam.service.entity.Training;
import com.epam.service.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByTraineeUsername(String username);
    List<Training> findByTrainerUsername(String username);
    @Query("SELECT t FROM Training t WHERE " +
            "t.trainer.username = :username AND " +
            "(:fromDate IS NULL OR t.trainingDate >= :fromDate) AND " +
            "(:toDate IS NULL OR t.trainingDate <= :toDate) AND " +
            "(:trainerName IS NULL OR t.trainer.firstName = :trainerName) AND " +
            "(:trainingType IS NULL OR t.trainingType = :trainingType)")
    List<Training> findTrainerTrainingsByCriteria(
            @Param("username") String username,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingType") TrainingType trainingType
    );
    @Query("SELECT t FROM Training t WHERE " +
            "t.trainee.username = :username AND " +
            "(:fromDate IS NULL OR t.trainingDate >= :fromDate) AND " +
            "(:toDate IS NULL OR t.trainingDate <= :toDate) AND " +
            "(:trainerName IS NULL OR t.trainee.firstName = :trainerName) AND " +
            "(:trainingType IS NULL OR t.trainingType = :trainingType)")
    List<Training> findTraineeTrainingsByCriteria(
            @Param("username") String username,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingType") TrainingType trainingType

    );

}