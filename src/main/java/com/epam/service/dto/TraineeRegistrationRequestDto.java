package com.epam.service.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRegistrationRequestDto {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
}