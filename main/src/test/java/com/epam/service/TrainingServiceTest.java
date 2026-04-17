package com.epam.service;

import com.epam.service.client.WorkloadClient;
import com.epam.service.dto.WorkloadRequestDto;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.Training;
import com.epam.service.entity.TrainingType;
import com.epam.service.entity.User;
import com.epam.service.repository.TrainingRepository;
import com.epam.service.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
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

    @Mock
    private WorkloadClient workloadClient;

    @Test
    void testCreateTraining() {
        User trainerUser = User.builder().username("trainer.user").firstName("Trainer").lastName("User").isActive(true).build();
        Trainer trainer = Trainer.builder().user(trainerUser).build();
        User traineeUser = User.builder().username("trainee.user").firstName("Trainee").lastName("User").build();
        Trainee trainee = Trainee.builder().user(traineeUser).build();

        Training training = Training.builder()
                .id(1L)
                .trainee(trainee)
                .trainer(trainer)
                .trainingName("Java Training")
                .trainingType(TrainingType.CARDIO)
                .trainingDate(new Date())
                .trainingDuration(Duration.ofMinutes(60))
                .build();
        when(trainingRepository.save(any(Training.class))).thenReturn(training);
        doNothing().when(workloadClient).handleWorkload(any(WorkloadRequestDto.class));

        Training createdTraining = trainingService.createTraining(training);

        assertEquals(training, createdTraining);
        verify(trainingRepository, times(1)).save(training);
        verify(workloadClient, times(1)).handleWorkload(any(WorkloadRequestDto.class));
    }

    @Test
    void testSelectTraining() {
        Training training = Training.builder()
                .id(1L)
                .trainee(new Trainee())
                .trainer(new Trainer())
                .trainingName("Java Training")
                .trainingType(TrainingType.CARDIO)
                .trainingDate(new Date())
                .trainingDuration(Duration.ofMinutes(60))
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
        User trainerUser = User.builder().username("trainer.user").firstName("Trainer").lastName("User").isActive(true).build();
        Trainer trainer = Trainer.builder().user(trainerUser).build();
        User traineeUser = User.builder().username("trainee.user").firstName("Trainee").lastName("User").build();
        Trainee trainee = Trainee.builder().user(traineeUser).build();
        Training training = Training.builder()
                .id(trainingId)
                .trainee(trainee)
                .trainer(trainer)
                .trainingDate(new Date())
                .trainingDuration(Duration.ofMinutes(60))
                .trainingType(TrainingType.CARDIO)
                .build();

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
        doNothing().when(workloadClient).handleWorkload(any(WorkloadRequestDto.class));
        doNothing().when(trainingRepository).deleteById(trainingId);

        trainingService.deleteTraining(trainingId);

        verify(trainingRepository, times(1)).findById(trainingId);
        verify(workloadClient, times(1)).handleWorkload(any(WorkloadRequestDto.class));
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