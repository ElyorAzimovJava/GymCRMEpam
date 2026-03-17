package com.epam.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.Duration;
import java.util.Date;

@Data
public class TrainingRequestDto {
    @NotBlank
    private String traineeUsername;
    @NotBlank
    private String trainerUsername;
    @NotBlank
    private String trainingName;
    @NotNull
    private Date trainingDate;
    @NotNull
    private Duration trainingDuration;
}