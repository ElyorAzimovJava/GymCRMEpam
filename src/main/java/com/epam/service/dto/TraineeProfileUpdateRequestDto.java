package com.epam.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeProfileUpdateRequestDto {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private boolean isActive;

}
