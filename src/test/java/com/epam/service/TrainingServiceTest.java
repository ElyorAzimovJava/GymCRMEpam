package com.epam.service;

import com.epam.service.dao.TrainingRepository;
import com.epam.service.model.Trainee;
import com.epam.service.model.Trainer;
import com.epam.service.model.Training;
import com.epam.service.model.TrainingType;
import com.epam.service.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingRepository trainingRepository;

    @Test
    void testCreateTraining() {
        Training training = Training.builder()
                .id(1)
                .trainee(new Trainee())
                .trainer(new Trainer())
                .trainingName("Java Training")
                .trainingType(TrainingType.YOGA)
                .trainingDate(new Date())
                .trainingDuration(60)
                .build();
        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        Training createdTraining = trainingService.createTraining(training);

        assertEquals(training, createdTraining);
        verify(trainingRepository, times(1)).save(training);
    }

    @Test
    void testSelectTraining() {
        Training training = Training.builder()
                .id(1L)
                .trainee(new Trainee())
                .trainer(new Trainer())
                .trainingName("Java Training")
                .trainingType(TrainingType.YOGA)
                .trainingDate(new Date())
                .trainingDuration(60)
                .build();
        when(trainingRepository.findById(1L)).thenReturn(Optional.of(training));

        Training selectedTraining = trainingService.selectTraining(1L);

        assertEquals(training, selectedTraining);
        verify(trainingRepository, times(1)).findById(1L);
    }

    @Test
    void testSelectTrainingNotFound() {
        when(trainingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> trainingService.selectTraining(1L));
        verify(trainingRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTraining() {
        long trainingId = 1L;
        doNothing().when(trainingRepository).deleteById(trainingId);
        trainingService.deleteTraining(trainingId);
        verify(trainingRepository, times(1)).deleteById(trainingId);
    }

    @Test
    void testSelectAllTrainings() {
        when(trainingRepository.findAll()).thenReturn(Collections.emptyList());

        List<Training> trainings = trainingService.selectAllTrainings();

        assertEquals(0, trainings.size());
        verify(trainingRepository, times(1)).findAll();
    }
}