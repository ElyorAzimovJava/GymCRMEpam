package com.epam.service.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.dao.TrainerDAO;
import com.epam.service.model.Trainee;
import com.epam.service.model.Trainer;
import com.epam.service.service.UserUsername;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserUsernameTest {

    private UserUsername userUsername;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userUsername = new UserUsername(traineeDAO, trainerDAO);
    }

    @Test
    public void testGenerateUsername() {
        when(traineeDAO.findAll()).thenReturn(new ArrayList<>());
        when(trainerDAO.findAll()).thenReturn(new ArrayList<>());

        String username = userUsername.generateUsername("John", "Doe");
        assertEquals("John.Doe", username);
    }

    @Test
    public void testGenerateUsernameWhenTaken() {
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(Trainee.builder().username("John.Doe").build());
        when(traineeDAO.findAll()).thenReturn(trainees);
        when(trainerDAO.findAll()).thenReturn(new ArrayList<>());

        String username = userUsername.generateUsername("John", "Doe");
        assertEquals("John.Doe1", username);
    }

    @Test
    public void testGenerateUsernameWhenTakenMultipleTimes() {
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(Trainee.builder().username("John.Doe").build());
        trainees.add(Trainee.builder().username("John.Doe1").build());
        when(traineeDAO.findAll()).thenReturn(trainees);

        List<Trainer> trainers = new ArrayList<>();
        trainers.add(Trainer.builder().username("John.Doe2").build());
        when(trainerDAO.findAll()).thenReturn(trainers);

        String username = userUsername.generateUsername("John", "Doe");
        assertEquals("John.Doe3", username);
    }
}