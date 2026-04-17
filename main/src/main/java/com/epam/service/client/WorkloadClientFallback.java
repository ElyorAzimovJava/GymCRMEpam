package com.epam.service.client;

import com.epam.service.dto.WorkloadRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkloadClientFallback implements WorkloadClient {

    @Override
    public void handleWorkload(WorkloadRequestDto request) {
        log.error("Error while calling workload-service. Fallback method executed.");
    }
}