package com.epam.service.controller;

import com.epam.service.dto.*;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.TrainingType;
import com.epam.service.entity.User;
import com.epam.service.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    @InjectMocks
    private TrainerController trainerController;

    @Mock
    private TrainerService trainerService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
    }

    @Test
    void testRegisterTrainer() throws Exception {
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

        mockMvc.perform(post("/trainers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Mike.Tyson"));

        verify(trainerService).createTrainer(any(Trainer.class));
    }

    @Test
    void testUpdateTrainerStatus() throws Exception {
        ActivationRequestDto request = new ActivationRequestDto();
        request.setActive(true);

        mockMvc.perform(patch("/trainers/Mike.Tyson/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(trainerService).activateTrainer("Mike.Tyson");

        request.setActive(false);
        mockMvc.perform(patch("/trainers/Mike.Tyson/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(trainerService).deactivateTrainer("Mike.Tyson");
    }

    @Test
    void testGetTrainerProfile() throws Exception {
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

        mockMvc.perform(get("/trainers/Mike.Tyson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mike"))
                .andExpect(jsonPath("$.lastName").value("Tyson"))
                .andExpect(jsonPath("$.specialization").value("CARDIO"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.trainees[0].username").value("John.Doe"))
                .andExpect(jsonPath("$.trainees[0].firstName").value("John"))
                .andExpect(jsonPath("$.trainees[0].lastName").value("Doe"));

        verify(trainerService).selectTrainerByUsername("Mike.Tyson");
    }

    @Test
    void testUpdateTrainerProfile() throws Exception {
        TrainerProfileUpdateRequestDto request = new TrainerProfileUpdateRequestDto();
        request.setFirstName("New");
        request.setLastName("Name");
        request.setSpecialization(TrainingType.CARDIO);
        request.setActive(true);

        User user = new User();
        user.setFirstName("New");
        user.setLastName("Name");
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(TrainingType.CARDIO);
        trainer.setTrainees(Collections.emptyList());

        when(trainerService.selectTrainerByUsername("Mike.Tyson")).thenReturn(trainer);
        when(trainerService.updateTrainer(any(Trainer.class))).thenReturn(trainer);

        mockMvc.perform(put("/trainers/Mike.Tyson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("Name"))
                .andExpect(jsonPath("$.specialization").value("CARDIO"))
                .andExpect(jsonPath("$.active").value(true));

        verify(trainerService).updateTrainer(any(Trainer.class));
    }
}