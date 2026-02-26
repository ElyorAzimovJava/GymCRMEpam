package com.epam.service;

import com.epam.service.dao.TraineeRepository;
import com.epam.service.model.Trainee;
import com.epam.service.service.TraineeService;
import com.epam.service.service.UserUsername;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserUsername userUsername;

    @Test
    void testCreateTrainee() {
        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .dateOfBirth(new Date())
                .address("123 Main St")
                .build();
        when(userUsername.generateUsername(anyString(), anyString())).thenReturn("John.Doe");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        assertNotNull(createdTrainee);
        assertEquals("John.Doe", createdTrainee.getUsername());
        assertNotNull(createdTrainee.getPassword());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testSelectTrainee() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .password("password")
                .isActive(true)
                .dateOfBirth(new Date())
                .address("123 Main St")
                .build();
        when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));

        Trainee selectedTrainee = traineeService.selectTrainee(1L);

        assertEquals(trainee, selectedTrainee);
        verify(traineeRepository, times(1)).findById(1L);
    }

    @Test
    void testSelectTraineeNotFound() {
        when(traineeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> traineeService.selectTrainee(1L));
        verify(traineeRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTrainee() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .dateOfBirth(new Date())
                .address("123 Main St")
                .build();
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        assertNotNull(updatedTrainee);
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testDeleteTrainee() {
        long traineeId = 1L;
        doNothing().when(traineeRepository).deleteById(traineeId);
        traineeService.deleteTrainee(traineeId);
        verify(traineeRepository, times(1)).deleteById(traineeId);
    }

    @Test
    void testSelectAllTrainees() {
        when(traineeRepository.findAll()).thenReturn(Collections.singletonList(Trainee.builder().build()));

        List<Trainee> trainees = traineeService.selectAllTrainees();

        assertNotNull(trainees);
        assertEquals(1, trainees.size());
        verify(traineeRepository, times(1)).findAll();
    }
}