package com.epam.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.model.Trainee;
import com.epam.service.service.TraineeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeDAO traineeDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee("John", "Doe", null, null, true, new Date(), "123 Main St");
        when(traineeDAO.save(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        assertNotNull(createdTrainee);
        assertEquals("John.Doe", createdTrainee.getUsername());
        assertNotNull(createdTrainee.getPassword());
    }

    @Test
    public void testSelectTrainee() {
        Trainee trainee = new Trainee("John", "Doe", "John.Doe", "password", true, new Date(), "123 Main St");
        when(traineeDAO.findById(1L)).thenReturn(Optional.of(trainee));

        Optional<Trainee> selectedTrainee = traineeService.selectTrainee(1L);

        assertEquals(trainee, selectedTrainee.get());
    }
}