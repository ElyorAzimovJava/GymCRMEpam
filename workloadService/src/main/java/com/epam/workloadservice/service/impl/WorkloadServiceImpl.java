package com.epam.workloadservice.service.impl;

import com.epam.workloadservice.client.TrainerClient;
import com.epam.workloadservice.dto.WorkloadRequestDto;
import com.epam.workloadservice.entity.TrainerWorkload;
import com.epam.workloadservice.enums.TrainingType;
import com.epam.workloadservice.exception.SpecializationMismatchException;
import com.epam.workloadservice.repository.WorkloadRepository;
import com.epam.workloadservice.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkloadServiceImpl implements WorkloadService {

    private final WorkloadRepository workloadRepository;
    private final TrainerClient trainerClient;

    @Override
    public void handleWorkload(WorkloadRequestDto request) {
        log.info("Handling workload request: {}", request);
        LocalDate trainingDate = request.getTrainingDate();
        int year = trainingDate.getYear();
        int month = trainingDate.getMonthValue();

        TrainerWorkload workload = workloadRepository.findByTrainerUsernameAndYearAndMonth(request.getTrainerUsername(), year, month)
                .orElseGet(() -> {
                    log.info("Creating new workload for trainer: {}, year: {}, month: {}", request.getTrainerUsername(), year, month);
                    TrainerWorkload newWorkload = new TrainerWorkload();
                    newWorkload.setTrainerUsername(request.getTrainerUsername());
                    newWorkload.setTrainerFirstName(request.getTrainerFirstName());
                    newWorkload.setTrainerLastName(request.getTrainerLastName());
                    newWorkload.setActive(request.isActive());
                    newWorkload.setYear(year);
                    newWorkload.setMonth(month);
                    newWorkload.setTrainingDurationSummary(Duration.ZERO);
                    return newWorkload;
                });

        if ("ADD".equalsIgnoreCase(request.getActionType())) {
            TrainingType trainerSpecialization = trainerClient.getTrainerSpecialization(request.getTrainerUsername());
            if (trainerSpecialization != request.getTrainingType()) {
                log.error("Trainer's specialization does not match the training type.");
                throw new SpecializationMismatchException("Trainer's specialization does not match the training type.");
            }
            workload.setTrainingDurationSummary(workload.getTrainingDurationSummary().plus(request.getTrainingDuration()));
            log.info("Added training duration to workload. New summary: {}", workload.getTrainingDurationSummary());
        } else if ("DELETE".equalsIgnoreCase(request.getActionType())) {
            workload.setTrainingDurationSummary(workload.getTrainingDurationSummary().minus(request.getTrainingDuration()));
            log.info("Subtracted training duration from workload. New summary: {}", workload.getTrainingDurationSummary());
        }

        workloadRepository.save(workload);
        log.info("Saved workload: {}", workload);
    }
}