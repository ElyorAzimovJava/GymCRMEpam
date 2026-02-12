package com.epam.service;

import com.epam.service.dao.TrainerDAO;
import com.epam.service.model.Trainer;
import com.epam.service.service.TrainerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TrainerServiceTest {

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerDAO trainerDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer("Jane", "Doe", null, null, true, "Java");
        when(trainerDAO.save(any(Trainer.class))).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        assertNotNull(createdTrainer);
        assertEquals("Jane.Doe", createdTrainer.getUsername());
        assertNotNull(createdTrainer.getPassword());
    }

    @Test
    public void testSelectTrainer() {
        Trainer trainer = new Trainer("Jane", "Doe", "Jane.Doe", "password", true, "Java");
        when(trainerDAO.findById(1L)).thenReturn(Optional.of(trainer));

        Optional<Trainer> selectedTrainer = trainerService.selectTrainer(1L);

        assertEquals(trainer, selectedTrainer.get());
    }
}