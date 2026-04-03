package com.epam.service.controller;

import com.epam.service.dto.*;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.TrainingType;
import com.epam.service.entity.User;
import com.epam.service.service.TraineeService;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private TraineeController traineeController;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
    }

    @Test
    void testRegisterTrainee() throws Exception {
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

        mockMvc.perform(post("/trainees/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("John.Doe"));

        verify(traineeService, times(1)).createTrainee(any(Trainee.class));
    }

    @Test
    void testUpdateTraineeStatus() throws Exception {
        ActivationRequestDto request = new ActivationRequestDto();
        request.setActive(true);

        mockMvc.perform(patch("/trainees/John.Doe/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(traineeService).activateTrainee("John.Doe");

        request.setActive(false);
        mockMvc.perform(patch("/trainees/John.Doe/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(traineeService).deactivateTrainee("John.Doe");
    }

    @Test
    void testUpdateTraineeTrainers() throws Exception {
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

        List<String> trainerUsernames = Collections.singletonList("trainer1");

        mockMvc.perform(put("/trainees/John.Doe/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerUsernames)))
                .andExpect(status().isOk());

        verify(traineeService).updateTrainee(any(Trainee.class));
    }

    @Test
    void testGetTraineeProfile() throws Exception {
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

        mockMvc.perform(get("/trainees/John.Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateTraineeProfile() throws Exception {
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

        mockMvc.perform(put("/trainees/John.Doe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(traineeService).updateTrainee(any(Trainee.class));
    }

    @Test
    void testDeleteTraineeProfile() throws Exception {
        doNothing().when(traineeService).deleteTraineeByUsername("John.Doe");

        mockMvc.perform(delete("/trainees/John.Doe"))
                .andExpect(status().isOk());

        verify(traineeService).deleteTraineeByUsername("John.Doe");
    }

    @Test
    void testGetNotAssignedTrainers() throws Exception {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("trainer1");
        user.setFirstName("Mike");
        user.setLastName("Tyson");

        trainer.setUser(user);
        trainer.setSpecialization(TrainingType.CARDIO);

        when(trainerService.getUnassignedTrainers("John.Doe"))
                .thenReturn(Collections.singletonList(trainer));

        mockMvc.perform(get("/trainees/John.Doe/trainers/not-assigned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("trainer1"));
    }
}