package com.epam.service;

import com.epam.service.dao.TraineeDAO;
import com.epam.service.dao.TrainerDAO;
import com.epam.service.model.Trainer;
import com.epam.service.model.TrainingType;
import com.epam.service.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TrainerServiceTest {

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TraineeDAO traineeDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .isActive(true)
                .specialization(TrainingType.CARDIO)
                .build();
        when(trainerDAO.save(any(Trainer.class))).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        assertNotNull(createdTrainer);
        assertNotNull(createdTrainer.getUsername());
        assertNotNull(createdTrainer.getPassword());
    }

    @Test
    public void testSelectTrainer() {
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .username("Jane.Doe")
                .password("password")
                .isActive(true)
                .specialization(TrainingType.CARDIO)
                .build();
        when(trainerDAO.findById(1L)).thenReturn(trainer);

        Trainer selectedTrainer = trainerService.selectTrainer(1L);

        assertEquals(trainer, selectedTrainer);
    }

    @Test
    public void testUpdateTrainer() {
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .isActive(true)
                .specialization(TrainingType.CARDIO)
                .build();
        when(trainerDAO.update(any(Trainer.class))).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        assertNotNull(updatedTrainer);
    }

    @Test
    public void testSelectAllTrainers() {
        when(trainerDAO.findAll()).thenReturn(Collections.singletonList(Trainer.builder().build()));

        List<Trainer> trainers = trainerService.selectAllTrainers();

        assertNotNull(trainers);
        assertEquals(1, trainers.size());
    }
}