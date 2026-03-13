package com.epam.service.dto;

import com.epam.service.entity.TrainingType;
import lombok.Data;

@Data
public class TrainerRegistrationRequestDto {
    private String firstName;
    private String lastName;
    private TrainingType specialization;
}