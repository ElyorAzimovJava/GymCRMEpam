package com.epam.service;

import com.epam.service.entity.User;
import com.epam.service.repository.TrainerRepository;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.TrainingType;
import com.epam.service.service.TraineeService;
import com.epam.service.service.TrainerService;
import com.epam.service.service.UserService;
import com.epam.service.service.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
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
class TrainerServiceTest {

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UsernameGenerator usernameGenerator;

    @Mock
    private UserService userService;

    @Mock
    private TraineeService traineeService;

    @Test
    void testCreateTrainer() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(TrainingType.CARDIO);

        when(usernameGenerator.generateUsername(anyString(), anyString())).thenReturn("Jane.Doe");
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        assertNotNull(createdTrainer);
        assertEquals("Jane.Doe", createdTrainer.getUser().getUsername());
        assertNotNull(createdTrainer.getUser().getPassword());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testSelectTrainer() {
        Trainer trainer = Trainer.builder()
                .id(1L)
                .user(User.builder()
                        .firstName("Jane")
                        .lastName("Doe")
                        .username("Jane.Doe")
                        .password("password")
                        .isActive(true).build())
                .specialization(TrainingType.CARDIO)
                .build();
        when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

        Trainer selectedTrainer = trainerService.selectTrainer(1L);

        assertEquals(trainer, selectedTrainer);
        verify(trainerRepository, times(1)).findById(1L);
    }

    @Test
    void testSelectTrainerNotFound() {
        when(trainerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> trainerService.selectTrainer(1L));
        verify(trainerRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = Trainer.builder()
                .id(1L)
                .user(User.builder()
                        .firstName("Jane")
                        .lastName("Doe")
                        .username("Jane.Doe")
                        .password("password")
                        .isActive(true).build())
                .build();
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        assertNotNull(updatedTrainer);
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testDeleteTrainer() {
        long trainerId = 1L;
        doNothing().when(trainerRepository).deleteById(trainerId);
        trainerService.deleteTrainer(trainerId);
        verify(trainerRepository, times(1)).deleteById(trainerId);
    }

    @Test
    void testSelectAllTrainers() {
        when(trainerRepository.findAll()).thenReturn(Collections.singletonList(Trainer.builder().build()));

        List<Trainer> trainers = trainerService.selectAllTrainers();

        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        verify(trainerRepository, times(1)).findAll();
    }

    @Test
    void testSelectTrainerByUsername() {
        User user = new User();
        user.setUsername("Jane.Doe");

        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);

        when(trainerRepository.findByUserUsername("Jane.Doe")).thenReturn(Optional.of(trainer));

        Trainer selectedTrainer = trainerService.selectTrainerByUsername("Jane.Doe");

        assertEquals(trainer, selectedTrainer);
        verify(trainerRepository, times(1)).findByUserUsername("Jane.Doe");
    }

    @Test
    void testSelectTrainerByUsernameNotFound() {
        when(trainerRepository.findByUserUsername("Jane.Doe")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> trainerService.selectTrainerByUsername("Jane.Doe"));
        verify(trainerRepository, times(1)).findByUserUsername("Jane.Doe");
    }

    @Test
    void testChangeTrainerPassword() {
        doNothing().when(userService).changePassword("Jane.Doe", "oldPassword", "newPassword");
        trainerService.changeTrainerPassword("Jane.Doe", "oldPassword", "newPassword");
        verify(userService, times(1)).changePassword("Jane.Doe", "oldPassword", "newPassword");
    }

    @Test
    void testActivateTrainer() {
        User user = new User();
        user.setUsername("Jane.Doe");
        user.setActive(false);

        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);

        when(trainerRepository.findByUserUsername("Jane.Doe")).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        trainerService.activateTrainer("Jane.Doe");

        assertEquals(true, trainer.getUser().isActive());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testDeactivateTrainer() {
        User user = new User();
        user.setUsername("Jane.Doe");
        user.setActive(true);

        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);

        when(trainerRepository.findByUserUsername("Jane.Doe")).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        trainerService.deactivateTrainer("Jane.Doe");

        assertEquals(false, trainer.getUser().isActive());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testGetUnassignedTrainers() {
        User traineeUser = new User();
        traineeUser.setUsername("John.Doe");

        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(traineeUser);

        User assignedTrainerUser = new User();
        assignedTrainerUser.setUsername("assigned.trainer");

        Trainer assignedTrainer = new Trainer();
        assignedTrainer.setId(1L);
        assignedTrainer.setUser(assignedTrainerUser);

        trainee.setTrainers(Collections.singletonList(assignedTrainer));

        User unassignedTrainerUser = new User();
        unassignedTrainerUser.setUsername("unassigned.trainer");

        Trainer unassignedTrainer = new Trainer();
        unassignedTrainer.setId(2L);
        unassignedTrainer.setUser(unassignedTrainerUser);

        when(traineeService.selectTraineeByUsername("John.Doe")).thenReturn(trainee);
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(assignedTrainer, unassignedTrainer));

        List<Trainer> unassignedTrainers = trainerService.getUnassignedTrainers("John.Doe");

        assertEquals(1, unassignedTrainers.size());
        assertEquals(unassignedTrainer, unassignedTrainers.get(0));
    }
}