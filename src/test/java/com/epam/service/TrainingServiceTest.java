package com.epam.service;

import com.epam.service.dao.TrainingDAO;
import com.epam.service.model.Training;
import com.epam.service.model.TrainingType;
import com.epam.service.service.TrainingService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TrainingServiceTest {

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingDAO trainingDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTraining() {
        TrainingType trainingType = new TrainingType("Java Development");
        Training training = new Training(1L, 1L, "Java Training", trainingType, new Date(), 60);
        when(trainingDAO.save(any(Training.class))).thenReturn(training);

        Training createdTraining = trainingService.createTraining(training);

        assertEquals(training, createdTraining);
    }

    @Test
    public void testSelectTraining() {
        TrainingType trainingType = new TrainingType("Java Development");
        Training training = new Training(1L, 1L, "Java Training", trainingType, new Date(), 60);
        when(trainingDAO.findById(1L)).thenReturn(Optional.of(training));

        Optional<Training> selectedTraining = trainingService.selectTraining(1L);

        assertEquals(training, selectedTraining.get());
    }
}