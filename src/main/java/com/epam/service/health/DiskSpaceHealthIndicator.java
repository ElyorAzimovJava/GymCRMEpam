package com.epam.service.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DiskSpaceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        File path = new File(".");
        long diskFreeInBytes = path.getFreeSpace();
        long threshold = 1024 * 1024 * 100; // 100MB

        if (diskFreeInBytes >= threshold) {
            return Health.up().withDetail("free_space", diskFreeInBytes + " bytes").build();
        } else {
            return Health.down().withDetail("free_space", diskFreeInBytes + " bytes").build();
        }
    }
}