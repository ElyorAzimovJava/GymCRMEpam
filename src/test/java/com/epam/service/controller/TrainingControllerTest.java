package com.epam.service.controller;

import com.epam.service.dto.TraineeTrainingResponseDto;
import com.epam.service.dto.TrainerTrainingResponseDto;
import com.epam.service.dto.TrainingRequestDto;
import com.epam.service.entity.*;
import com.epam.service.service.TraineeService;
import com.epam.service.service.TrainerService;
import com.epam.service.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @InjectMocks
    private TrainingController trainingController;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Test
    void testCreateTraining() {

        TrainingRequestDto request = new TrainingRequestDto();
        request.setTraineeUsername("John.Doe");
        request.setTrainerUsername("Mike.Tyson");
        request.setTrainingName("Morning Training");
        request.setTrainingDate(new Date());
        request.setTrainingDuration(Duration.ofMinutes(60));

        User traineeUser = new User();
        traineeUser.setUsername("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);

        User trainerUser = new User();
        trainerUser.setUsername("Mike.Tyson");

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        trainer.setSpecialization(TrainingType.CARDIO);

        when(traineeService.selectTraineeByUsername("John.Doe")).thenReturn(trainee);
        when(trainerService.selectTrainerByUsername("Mike.Tyson")).thenReturn(trainer);

        when(trainingService.createTraining(any(Training.class)))
                .thenReturn(new Training());

        ResponseEntity<Void> response = trainingController.createTraining(request);

        assertEquals(201, response.getStatusCodeValue());

        verify(trainingService, times(1)).createTraining(any(Training.class));
    }

    @Test
    void testGetTraineeTrainings() {

        User trainerUser = new User();
        trainerUser.setFirstName("Mike");

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);

        Training training = new Training();
        training.setTrainingName("Morning Training");
        training.setTrainingDate(new Date());
        training.setTrainingType(TrainingType.CARDIO);
        training.setTrainingDuration(Duration.ofMinutes(60));
        training.setTrainer(trainer);

        when(trainingService.getTraineeTrainings(anyString(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(training));

        ResponseEntity<List<TraineeTrainingResponseDto>> response =
                trainingController.getTraineeTrainings("John.Doe", null, null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(trainingService).getTraineeTrainings(anyString(), any(), any(), any(), any());
    }

    @Test
    void testGetTrainingTypes() {

        ResponseEntity<List<TrainingType>> response = trainingController.getTrainingTypes();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    void testGetTrainerTrainings() {

        User traineeUser = new User();
        traineeUser.setFirstName("John");

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);

        Training training = new Training();
        training.setTrainingName("Morning Training");
        training.setTrainingDate(new Date());
        training.setTrainingType(TrainingType.CARDIO);
        training.setTrainingDuration(Duration.ofMinutes(60));
        training.setTrainee(trainee);

        when(trainingService.getTrainerTrainings(anyString(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(training));

        ResponseEntity<List<TrainerTrainingResponseDto>> response =
                trainingController.getTrainerTrainings("Mike.Tyson", null, null, null, null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(trainingService).getTrainerTrainings(anyString(), any(), any(), any(), any());
    }
}