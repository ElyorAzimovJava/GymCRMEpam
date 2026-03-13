package com.epam.service.controller;

import com.epam.service.dto.*;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.TrainingType;
import com.epam.service.entity.User;
import com.epam.service.service.TraineeService;
import com.epam.service.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Test
    void testRegisterTrainee() {
        TraineeRegistrationRequestDto request = new TraineeRegistrationRequestDto();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setAddress("Street 1");
        request.setDateOfBirth(new Date());

        User user = new User();
        user.setUsername("John.Doe");
        user.setPassword("password");

        Trainee trainee = new Trainee();
        trainee.setUser(user);

        when(traineeService.createTrainee(any(Trainee.class))).thenReturn(trainee);

        ResponseEntity<RegistrationResponseDto> response =
                traineeController.registerTrainee(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("John.Doe", response.getBody().getUsername());
        assertEquals("password", response.getBody().getPassword());

        verify(traineeService, times(1)).createTrainee(any(Trainee.class));
    }

    @Test
    void testActivateTraineeStatus() {
        ActivationRequestDto request = new ActivationRequestDto();
        request.setActive(true);

        ResponseEntity<Void> response =
                traineeController.updateTraineeStatus("John.Doe", request);

        assertEquals(200, response.getStatusCodeValue());
        verify(traineeService).activateTrainee("John.Doe");
    }

    @Test
    void testDeactivateTraineeStatus() {
        ActivationRequestDto request = new ActivationRequestDto();
        request.setActive(false);

        ResponseEntity<Void> response =
                traineeController.updateTraineeStatus("John.Doe", request);

        assertEquals(200, response.getStatusCodeValue());
        verify(traineeService).deactivateTrainee("John.Doe");
    }

    @Test
    void testUpdateTraineeTrainers() {

        User traineeUser = new User();
        traineeUser.setUsername("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        User trainerUser = new User();
        trainerUser.setUsername("trainer1");

        Trainer trainer = new Trainer();
        trainer.setUser(trainerUser);
        trainer.setSpecialization(TrainingType.CARDIO);

        when(traineeService.selectTraineeByUsername("John.Doe")).thenReturn(trainee);
        when(trainerService.selectTrainerByUsername("trainer1")).thenReturn(trainer);
        when(traineeService.updateTrainee(any(Trainee.class))).thenReturn(trainee);

        ResponseEntity<List<TrainerDto>> response =
                traineeController.updateTraineeTrainers(
                        "John.Doe",
                        Collections.singletonList("trainer1")
                );

        assertEquals(200, response.getStatusCodeValue());
        verify(traineeService).updateTrainee(any(Trainee.class));
    }

    @Test
    void testGetTraineeProfile() {

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);

        Trainer trainer = new Trainer();
        User trainerUser = new User();
        trainerUser.setUsername("trainer1");
        trainerUser.setFirstName("Mike");
        trainerUser.setLastName("Tyson");
        trainer.setUser(trainerUser);
        trainer.setSpecialization(TrainingType.CARDIO);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress("Street 1");
        trainee.setDateOfBirth(new Date());
        trainee.setTrainers(Collections.singletonList(trainer));

        when(traineeService.selectTraineeByUsername("John.Doe")).thenReturn(trainee);

        ResponseEntity<TraineeProfileResponseDto> response =
                traineeController.getTraineeProfile("John.Doe");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    void testUpdateTraineeProfile() {

        User user = new User();
        user.setFirstName("Old");
        user.setLastName("Name");

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setTrainers(new ArrayList<>());

        TraineeProfileUpdateRequestDto request = new TraineeProfileUpdateRequestDto();
        request.setFirstName("New");
        request.setLastName("Name");
        request.setAddress("New Street");
        request.setDateOfBirth(new Date());
        request.setActive(true);

        when(traineeService.selectTraineeByUsername("John.Doe")).thenReturn(trainee);
        when(traineeService.updateTrainee(any(Trainee.class))).thenReturn(trainee);

        ResponseEntity<TraineeProfileResponseDto> response =
                traineeController.updateTraineeProfile("John.Doe", request);

        assertEquals(200, response.getStatusCodeValue());
        verify(traineeService).updateTrainee(any(Trainee.class));
    }

    @Test
    void testDeleteTraineeProfile() {

        doNothing().when(traineeService).deleteTraineeByUsername("John.Doe");

        ResponseEntity<Void> response =
                traineeController.deleteTraineeProfile("John.Doe");

        assertEquals(200, response.getStatusCodeValue());

        verify(traineeService).deleteTraineeByUsername("John.Doe");
    }

    @Test
    void testGetNotAssignedTrainers() {

        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("trainer1");
        user.setFirstName("Mike");
        user.setLastName("Tyson");

        trainer.setUser(user);
        trainer.setSpecialization(TrainingType.CARDIO);

        when(trainerService.getUnassignedTrainers("John.Doe"))
                .thenReturn(Collections.singletonList(trainer));

        ResponseEntity<List<TrainerDto>> response =
                traineeController.getNotAssignedTrainers("John.Doe");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}