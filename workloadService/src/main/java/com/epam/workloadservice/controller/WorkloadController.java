package com.epam.workloadservice.controller;

import com.epam.workloadservice.dto.WorkloadRequestDto;
import com.epam.workloadservice.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workload")
@RequiredArgsConstructor
public class WorkloadController {

    private final WorkloadService workloadService;

    @PostMapping
    public ResponseEntity<Void> handleWorkload(@RequestBody WorkloadRequestDto request) {
        workloadService.handleWorkload(request);
        return ResponseEntity.ok().build();
    }
}