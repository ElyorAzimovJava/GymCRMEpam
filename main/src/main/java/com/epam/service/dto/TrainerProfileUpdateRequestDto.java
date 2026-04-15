package com.epam.service.dto;

import com.epam.service.entity.TrainingType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileUpdateRequestDto {
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean isActive;
}