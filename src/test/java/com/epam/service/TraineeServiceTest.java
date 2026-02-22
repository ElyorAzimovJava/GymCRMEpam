package com.epam.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.dao.TrainerDAO;
import com.epam.service.model.Trainee;
import com.epam.service.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    // TODO:
    //  Replace manual Mockito initialization with MockitoExtension.
    //  Using @ExtendWith(MockitoExtension.class) removes boilerplate,
    //  ensures proper lifecycle handling, enables strict stubbing validation,
    //  and is the recommended approach for JUnit 5 tests
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .dateOfBirth(new Date())
                .address("123 Main St")
                .build();
        when(traineeDAO.findAll()).thenReturn(Collections.emptyList());
        when(trainerDAO.findAll()).thenReturn(Collections.emptyList());
        when(traineeDAO.save(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        assertNotNull(createdTrainee);
        assertNotNull(createdTrainee.getUsername());
        assertNotNull(createdTrainee.getPassword());
    }

    @Test
    public void testSelectTrainee() {
        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .password("password")
                .isActive(true)
                .dateOfBirth(new Date())
                .address("123 Main St")
                .build();
        when(traineeDAO.findById(1L)).thenReturn(trainee);

        Trainee selectedTrainee = traineeService.selectTrainee(1L);

        assertEquals(trainee, selectedTrainee);
    }

    @Test
    public void testUpdateTrainee() {
        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .dateOfBirth(new Date())
                .address("123 Main St")
                .build();
        when(traineeDAO.update(any(Trainee.class))).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        assertNotNull(updatedTrainee);
    }

    @Test
    public void testSelectAllTrainees() {
        when(traineeDAO.findAll()).thenReturn(Collections.singletonList(Trainee.builder().build()));

        List<Trainee> trainees = traineeService.selectAllTrainees();

        assertNotNull(trainees);
        assertEquals(1, trainees.size());
    }
}