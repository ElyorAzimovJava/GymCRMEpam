package com.epam.service;

import com.epam.service.dao.TrainingDAO;
import com.epam.service.model.Training;
import com.epam.service.model.TrainingType;
import com.epam.service.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TrainingServiceTest {

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingDAO trainingDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTraining() {
        Training training = Training.builder()
                .traineeId(1L)
                .trainerId(1L)
                .trainingName("Java Training")
                .trainingType(TrainingType.YOGA)
                .trainingDate(new Date())
                .trainingDuration(60)
                .build();
        when(trainingDAO.save(any(Training.class))).thenReturn(training);

        Training createdTraining = trainingService.createTraining(training);

        assertEquals(training, createdTraining);
    }

    @Test
    public void testSelectTraining() {
        Training training = Training.builder()
                .traineeId(1L)
                .trainerId(1L)
                .trainingName("Java Training")
                .trainingType(TrainingType.YOGA)
                .trainingDate(new Date())
                .trainingDuration(60)
                .build();
        when(trainingDAO.findById(1L)).thenReturn(Optional.of(training));

        Optional<Training> selectedTraining = trainingService.selectTraining(1L);

        assertEquals(training, selectedTraining.get());
    }
}