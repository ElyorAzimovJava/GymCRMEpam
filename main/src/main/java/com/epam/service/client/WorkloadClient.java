package com.epam.service.client;

import com.epam.service.dto.WorkloadRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "workload-service", url = "http://localhost:8082", fallback = WorkloadClientFallback.class)
public interface WorkloadClient {

    @PostMapping("/workload")
    void handleWorkload(@RequestBody WorkloadRequestDto request);
}