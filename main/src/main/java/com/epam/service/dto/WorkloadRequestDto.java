package com.epam.service.dto;

import com.epam.service.entity.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkloadRequestDto {
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private String traineeUsername;
    private String traineeFirstName;
    private String traineeLastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private Duration trainingDuration;
    private TrainingType trainingType;
    private String actionType;
}