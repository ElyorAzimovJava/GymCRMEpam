package com.epam.service;

import com.epam.service.repository.TrainingRepository;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.Training;
import com.epam.service.entity.TrainingType;
import com.epam.service.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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

    @Test
    void testGetTraineeTrainings() {
        Training training1 = Training.builder()
                .trainingDate(new Date(System.currentTimeMillis() - 100000))
                .trainer(Trainer.builder().firstName("trainer1").build())
                .trainingType(TrainingType.CARDIO)
                .build();
        Training training2 = Training.builder()
                .trainingDate(new Date(System.currentTimeMillis() + 100000))
                .trainer(Trainer.builder().firstName("trainer2").build())
                .trainingType(TrainingType.YOGA)
                .build();
        when(trainingRepository.findByTraineeUsername("John.Doe")).thenReturn(Arrays.asList(training1, training2));

        List<Training> trainings = trainingService.getTraineeTrainings("John.Doe", null, null, null, null);
        assertEquals(2, trainings.size());

        trainings = trainingService.getTraineeTrainings("John.Doe", new Date(), null, null, null);
        assertEquals(1, trainings.size());

        trainings = trainingService.getTraineeTrainings("John.Doe", null, new Date(), null, null);
        assertEquals(1, trainings.size());

        trainings = trainingService.getTraineeTrainings("John.Doe", null, null, "trainer1", null);
        assertEquals(1, trainings.size());

        trainings = trainingService.getTraineeTrainings("John.Doe", null, null, null, TrainingType.CARDIO);
        assertEquals(1, trainings.size());
    }

    @Test
    void testGetTrainerTrainings() {
        Training training1 = Training.builder()
                .trainingDate(new Date(System.currentTimeMillis() - 100000))
                .trainee(Trainee.builder().firstName("trainee1").build())
                .build();
        Training training2 = Training.builder()
                .trainingDate(new Date(System.currentTimeMillis() + 100000))
                .trainee(Trainee.builder().firstName("trainee2").build())
                .build();
        when(trainingRepository.findByTrainerUsername("Jane.Doe")).thenReturn(Arrays.asList(training1, training2));

        List<Training> trainings = trainingService.getTrainerTrainings("Jane.Doe", null, null, null);
        assertEquals(2, trainings.size());

        trainings = trainingService.getTrainerTrainings("Jane.Doe", new Date(), null, null);
        assertEquals(1, trainings.size());

        trainings = trainingService.getTrainerTrainings("Jane.Doe", null, new Date(), null);
        assertEquals(1, trainings.size());

        trainings = trainingService.getTrainerTrainings("Jane.Doe", null, null, "trainee1");
        assertEquals(1, trainings.size());
    }
}