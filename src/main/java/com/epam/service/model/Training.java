package com.epam.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    private static final AtomicLong idCounter = new AtomicLong();

    private long id;
    private long traineeId;
    private long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private Date trainingDate;
    private int trainingDuration;

    public Training(long traineeId, long trainerId, String trainingName, TrainingType trainingType, Date trainingDate, int trainingDuration) {
        this.id = idCounter.incrementAndGet();
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }
}