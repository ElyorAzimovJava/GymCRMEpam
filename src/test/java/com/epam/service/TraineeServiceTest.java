package com.epam.service;

import com.epam.service.entity.User;
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
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 com.epam.Main St");

        when(usernameGenerator.generateUsername(anyString(), anyString())).thenReturn("John.Doe");
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        assertNotNull(createdTrainee);
        assertEquals("John.Doe", createdTrainee.getUser().getUsername());
        assertNotNull(createdTrainee.getUser().getPassword());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testSelectTrainee() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("John.Doe");
        user.setPassword("password");
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 com.epam.Main St");

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
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 com.epam.Main St");

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
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("John.Doe")).thenReturn(Optional.of(trainee));

        Trainee selectedTrainee = traineeService.selectTraineeByUsername("John.Doe");

        assertEquals(trainee, selectedTrainee);
        verify(traineeRepository, times(1)).findByUserUsername("John.Doe");
    }

    @Test
    void testSelectTraineeByUsernameNotFound() {
        when(traineeRepository.findByUserUsername("John.Doe")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> traineeService.selectTraineeByUsername("John.Doe"));
        verify(traineeRepository, times(1)).findByUserUsername("John.Doe");
    }

    @Test
    void testChangeTraineePassword() {
        doNothing().when(userService).changePassword("John.Doe", "oldPassword","newPassword");
        traineeService.changeTraineePassword("John.Doe", "oldPassword","newPassword");
        verify(userService, times(1)).changePassword("John.Doe", "oldPassword","newPassword");
    }

    @Test
    void testActivateTrainee() {
        User user = new User();
        user.setUsername("John.Doe");
        user.setActive(false);

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("John.Doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.activateTrainee("John.Doe");

        assertEquals(true, trainee.getUser().isActive());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testDeactivateTrainee() {
        User user = new User();
        user.setUsername("John.Doe");
        user.setActive(true);

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("John.Doe")).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.deactivateTrainee("John.Doe");

        assertEquals(false, trainee.getUser().isActive());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void testDeleteTraineeByUsername() {
        User user = new User();
        user.setUsername("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);

        when(traineeRepository.findByUserUsername("John.Doe")).thenReturn(Optional.of(trainee));
        doNothing().when(traineeRepository).delete(trainee);

        traineeService.deleteTraineeByUsername("John.Doe");

        verify(traineeRepository, times(1)).delete(trainee);
    }

    @Test
    void testUpdateTraineeTrainers() {
        User traineeUser = new User();
        traineeUser.setUsername("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(traineeUser);
        trainee.setTrainers(new ArrayList<>());

        User trainerUser1 = new User();
        trainerUser1.setUsername("trainer1");

        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        trainer1.setUser(trainerUser1);

        User trainerUser2 = new User();
        trainerUser2.setUsername("trainer2");

        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        trainer2.setUser(trainerUser2);

        when(traineeRepository.findByUserUsername("John.Doe")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUserUsername("trainer1")).thenReturn(Optional.of(trainer1));
        when(trainerRepository.findByUserUsername("trainer2")).thenReturn(Optional.of(trainer2));
        when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

        traineeService.updateTraineeTrainers("John.Doe", Arrays.asList("trainer1", "trainer2"));

        assertEquals(2, trainee.getTrainers().size());
        verify(traineeRepository, times(1)).save(trainee);
    }
}