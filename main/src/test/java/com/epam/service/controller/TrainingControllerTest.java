package com.epam.service.controller;

import com.epam.service.dto.TrainingRequestDto;
import com.epam.service.entity.*;
import com.epam.service.service.TraineeService;
import com.epam.service.service.TrainerService;
import com.epam.service.service.TrainingService;
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

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void testCreateTraining() throws Exception {
        TrainingRequestDto request = new TrainingRequestDto();
        request.setTraineeUsername("John.Doe");
        request.setTrainerUsername("Mike.Tyson");
        request.setTrainingName("Morning Training");
        request.setTrainingDate(new Date());
        request.setTrainingDuration(Duration.ofMinutes(60));

        when(traineeService.selectTraineeByUsername(anyString())).thenReturn(new Trainee());
        when(trainerService.selectTrainerByUsername(anyString())).thenReturn(new Trainer());

        mockMvc.perform(post("/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(trainingService).createTraining(any(Training.class));
    }

    @Test
    void testGetTraineeTrainings() throws Exception {
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

        mockMvc.perform(get("/trainings/trainee/John.Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].trainingName", is("Morning Training")));

        verify(trainingService).getTraineeTrainings(anyString(), any(), any(), any(), any());
    }

    @Test
    void testGetTrainerTrainings() throws Exception {
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

        mockMvc.perform(get("/trainings/trainer/Mike.Tyson"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].trainingName", is("Morning Training")))
                .andExpect(jsonPath("$[0].traineeName", is("John")));

        verify(trainingService).getTrainerTrainings(anyString(), any(), any(), any(), any());
    }

    @Test
    void testGetTrainingTypes() throws Exception {
        mockMvc.perform(get("/trainings/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }
}