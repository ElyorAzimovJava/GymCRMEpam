package com.epam.service.dto;

import lombok.Data;

import java.time.Duration;
import java.time.Duration;
import java.util.Date;

@Data
public class TrainingRequestDto {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private Date trainingDate;
    private Duration trainingDuration;
}