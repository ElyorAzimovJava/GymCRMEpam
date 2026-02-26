package com.epam.service;

import com.epam.service.dao.TrainerRepository;
import com.epam.service.model.Trainer;
import com.epam.service.model.TrainingType;
import com.epam.service.service.TrainerService;
import com.epam.service.service.UserUsername;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private UserUsername userUsername;

    @Test
    void testCreateTrainer() {
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .isActive(true)
                .specialization(TrainingType.CARDIO)
                .build();
        when(userUsername.generateUsername(anyString(), anyString())).thenReturn("Jane.Doe");
        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        assertNotNull(createdTrainer);
        assertEquals("Jane.Doe", createdTrainer.getUsername());
        assertNotNull(createdTrainer.getPassword());
        verify(trainerRepository, times(1)).save(trainer);
    }

    @Test
    void testSelectTrainer() {
        Trainer trainer = Trainer.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .username("Jane.Doe")
                .password("password")
                .isActive(true)
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
                .firstName("Jane")
                .lastName("Doe")
                .isActive(true)
                .specialization(TrainingType.CARDIO)
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
}