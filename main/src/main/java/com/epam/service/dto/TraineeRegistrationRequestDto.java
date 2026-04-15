package com.epam.service.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRegistrationRequestDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Date dateOfBirth;
    @NotBlank
    private String address;
}