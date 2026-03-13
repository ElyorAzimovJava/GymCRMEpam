package com.epam.service.controller;

import com.epam.service.dto.*;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.TrainingType;
import com.epam.service.entity.User;
import com.epam.service.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerService trainerService;

    @Test
    void testRegisterTrainer() {

        TrainerRegistrationRequestDto request = new TrainerRegistrationRequestDto();
        request.setFirstName("Mike");
        request.setLastName("Tyson");
        request.setSpecialization(TrainingType.CARDIO);

        User user = new User();
        user.setUsername("Mike.Tyson");
        user.setPassword("password");

        Trainer trainer = new Trainer();
        trainer.setUser(user);

        when(trainerService.createTrainer(any(Trainer.class))).thenReturn(trainer);

        ResponseEntity<RegistrationResponseDto> response =
                trainerController.registerTrainer(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Mike.Tyson", response.getBody().getUsername());
        assertEquals("password", response.getBody().getPassword());

        verify(trainerService, times(1)).createTrainer(any(Trainer.class));
    }

    @Test
    void testActivateTrainerStatus() {

        ActivationRequestDto request = new ActivationRequestDto();
        request.setActive(true);

        ResponseEntity<Void> response =
                trainerController.updateTrainerStatus("Mike.Tyson", request);

        assertEquals(200, response.getStatusCodeValue());

        verify(trainerService).activateTrainer("Mike.Tyson");
    }

    @Test
    void testDeactivateTrainerStatus() {

        ActivationRequestDto request = new ActivationRequestDto();
        request.setActive(false);

        ResponseEntity<Void> response =
                trainerController.updateTrainerStatus("Mike.Tyson", request);

        assertEquals(200, response.getStatusCodeValue());

        verify(trainerService).deactivateTrainer("Mike.Tyson");
    }

    @Test
    void testGetTrainerProfile() {

        User user = new User();
        user.setFirstName("Mike");
        user.setLastName("Tyson");
        user.setActive(true);

        Trainee trainee = new Trainee();
        User traineeUser = new User();
        traineeUser.setUsername("John.Doe");
        traineeUser.setFirstName("John");
        traineeUser.setLastName("Doe");

        trainee.setUser(traineeUser);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(TrainingType.CARDIO);
        trainer.setTrainees(Collections.singletonList(trainee));

        when(trainerService.selectTrainerByUsername("Mike.Tyson")).thenReturn(trainer);

        ResponseEntity<TrainerProfileResponseDto> response =
                trainerController.getTrainerProfile("Mike.Tyson");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Mike", response.getBody().getFirstName());
        assertEquals(1, response.getBody().getTrainees().size());

        verify(trainerService).selectTrainerByUsername("Mike.Tyson");
    }

    @Test
    void testUpdateTrainerProfile() {

        User user = new User();
        user.setFirstName("Old");
        user.setLastName("Name");

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(TrainingType.YOGA);
        trainer.setTrainees(Collections.emptyList());

        TrainerProfileUpdateRequestDto request = new TrainerProfileUpdateRequestDto();
        request.setFirstName("New");
        request.setLastName("Name");
        request.setSpecialization(TrainingType.CARDIO);
        request.setActive(true);

        when(trainerService.selectTrainerByUsername("Mike.Tyson")).thenReturn(trainer);
        when(trainerService.updateTrainer(any(Trainer.class))).thenReturn(trainer);

        ResponseEntity<TrainerProfileResponseDto> response =
                trainerController.updateTrainerProfile("Mike.Tyson", request);

        assertEquals(200, response.getStatusCodeValue());

        verify(trainerService).updateTrainer(any(Trainer.class));
    }
}