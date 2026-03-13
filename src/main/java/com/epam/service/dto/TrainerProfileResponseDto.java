package com.epam.service.dto;

import com.epam.service.entity.TrainingType;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfileResponseDto {
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean isActive;
    private List<TraineeDto> trainees;
}