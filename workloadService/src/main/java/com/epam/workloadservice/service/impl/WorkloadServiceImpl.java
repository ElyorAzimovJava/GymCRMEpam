package com.epam.workloadservice.service.impl;

import com.epam.workloadservice.dto.WorkloadRequestDto;
import com.epam.workloadservice.entity.TrainerWorkload;
import com.epam.workloadservice.repository.WorkloadRepository;
import com.epam.workloadservice.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkloadServiceImpl implements WorkloadService {

    private final WorkloadRepository workloadRepository;

    @Override
    public void handleWorkload(WorkloadRequestDto request) {
        if ("ADD".equalsIgnoreCase(request.getActionType())) {
            TrainerWorkload workload = new TrainerWorkload();
            workload.setTrainerUsername(request.getTrainerUsername());
            workload.setTrainerFirstName(request.getTrainerFirstName());
            workload.setTrainerLastName(request.getTrainerLastName());
            workload.setActive(request.isActive());
            workload.setTrainingDate(request.getTrainingDate());
            workload.setTrainingDuration(request.getTrainingDuration());
            workloadRepository.save(workload);
        } else if ("DELETE".equalsIgnoreCase(request.getActionType())) {
            workloadRepository.findByTrainerUsernameAndTrainingDateAndTrainingDuration(
                    request.getTrainerUsername(),
                    request.getTrainingDate(),
                    request.getTrainingDuration()
            ).ifPresent(workloadRepository::delete);
        }
    }
}