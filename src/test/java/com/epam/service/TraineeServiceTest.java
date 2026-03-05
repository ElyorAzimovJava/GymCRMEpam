package com.epam.service;

import com.epam.service.repository.TraineeRepository;
import com.epam.service.repository.TrainerRepository;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.service.TraineeService;
import com.epam.service.service.UserService;
import com.epam.service.service.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

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
    private UsernameGenerator usernameGenerator;

    @Mock
    private UserService userService;

    @Mock
    private TrainerRepository trainerRepository;

    @Test
    void testCreateTrainee() {
        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .dateOfBirth(new Date())
                .address("123 Main St")
                .build();
        when(usernameGenerator.generateUsername(anyString(), anyString())).thenReturn("John.Doe");
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

    @Test
    void testSelectTraineeByUsername() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .build();
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));

        Trainee selectedTrainee = traineeService.selectTraineeByUsername("John.Doe");

        assertEquals(trainee, selectedTrainee);
        verify(traineeRepository, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testSelectTraineeByUsernameNotFound() {
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> traineeService.selectTraineeByUsername("John.Doe"));
        verify(traineeRepository, times(1)).findByUsername("John.Doe");
    }

    @Test
    void testChangeTraineePassword() {
        doNothing().when(userService).changePassword("John.Doe", "newPassword");
        traineeService.changeTraineePassword("John.Doe", "newPassword");
        verify(userService, times(1)).changePassword("John.Doe", "newPassword");
    }

    @Test
    void testActivateTrainee() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .username("John.Doe")
                .isActive(false)
                .build();
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.activateTrainee("John.Doe");

        assertEquals(true, trainee.isActive());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testDeactivateTrainee() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .username("John.Doe")
                .isActive(true)
                .build();
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.deactivateTrainee("John.Doe");

        assertEquals(false, trainee.isActive());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testDeleteTraineeByUsername() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .username("John.Doe")
                .build();
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).delete(trainee);

        traineeService.deleteTraineeByUsername("John.Doe");

        verify(traineeRepository, times(1)).delete(trainee);
    }

    @Test
    void testUpdateTraineeTrainers() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .username("John Doe")
                .trainers(new ArrayList<>())
                .build();
        Trainer trainer1 = Trainer.builder().id(1L).username("trainer1").build();
        Trainer trainer2 = Trainer.builder().id(2L).username("trainer2").build();
        when(traineeRepository.findByUsername("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername("trainer1")).thenReturn(Optional.of(trainer1));
        when(trainerRepository.findByUsername("trainer2")).thenReturn(Optional.of(trainer2));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.updateTraineeTrainers("John.Doe", Arrays.asList("trainer1", "trainer2"));

        assertEquals(2, trainee.getTrainers().size());
        verify(traineeRepository, times(1)).save(trainee);
    }
}