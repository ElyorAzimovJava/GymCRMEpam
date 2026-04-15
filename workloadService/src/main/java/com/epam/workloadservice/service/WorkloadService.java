package com.epam.workloadservice.service;

import com.epam.workloadservice.dto.WorkloadRequestDto;

public interface WorkloadService {
    void handleWorkload(WorkloadRequestDto request);
}