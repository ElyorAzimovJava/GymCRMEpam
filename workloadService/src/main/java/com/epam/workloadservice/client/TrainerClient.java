package com.epam.workloadservice.client;

import com.epam.workloadservice.enums.TrainingType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "trainer-service")
public interface TrainerClient {

    @GetMapping("/trainer/specialization/{username}")
    TrainingType getTrainerSpecialization(@PathVariable("username") String username);
}