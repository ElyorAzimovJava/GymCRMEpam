package com.epam.service.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeProfileResponseDto {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerDto> trainers;
}