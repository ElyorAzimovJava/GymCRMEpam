package com.epam.service.dto;

import com.epam.service.entity.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Duration;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainingResponseDto {
    private String trainingName;
    private Date trainingDate;
    private TrainingType trainingType;
    private Duration trainingDuration;
    private String trainerName;
}