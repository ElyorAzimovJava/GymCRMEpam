package com.epam.service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter traineeCreations;
    private final Counter trainerCreations;

    public CustomMetrics(MeterRegistry registry) {
        this.traineeCreations = Counter.builder("creations.total")
                .tag("entity", "trainee")
                .description("Total number of created trainees")
                .register(registry);

        this.trainerCreations = Counter.builder("creations.total")
                .tag("entity", "trainer")
                .description("Total number of created trainers")
                .register(registry);
    }

    public void incrementTraineeCreation() {
        traineeCreations.increment();
    }

    public void incrementTrainerCreation() {
        trainerCreations.increment();
    }
}