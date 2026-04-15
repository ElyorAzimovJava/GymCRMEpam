package com.epam.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActivationRequestDto {
    @NotNull
    private boolean isActive;
}