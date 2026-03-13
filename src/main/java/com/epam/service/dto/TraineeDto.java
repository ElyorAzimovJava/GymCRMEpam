package com.epam.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {
    private String username;
    private String firstName;
    private String lastName;
}