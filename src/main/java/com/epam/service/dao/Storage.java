package com.epam.service.dao;

import com.epam.service.model.Trainee;
import com.epam.service.model.Trainer;
import com.epam.service.model.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class Storage {

    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    private final Map<Long, Trainee> traineeStore = new HashMap<>();
    private final Map<Long, Trainer> trainerStore = new HashMap<>();
    private final Map<Long, Training> trainingStore = new HashMap<>();

    @Value("${initial.data.path}")
    private String initialDataPath;

    @PostConstruct
    public void initialize() {
        logger.info("Initializing storage from file: {}", initialDataPath);
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(initialDataPath)) {
            properties.load(input);
            logger.info("Initial data loaded successfully.");
            loadTrainees(properties);
            loadTrainers(properties);
        } catch (IOException ex) {
            logger.error("Failed to load initial data from file: {}", initialDataPath, ex);
        }
    }

    private void loadTrainees(Properties properties) {
        properties.stringPropertyNames().stream()
                .filter(key -> key.startsWith("trainee."))
                .map(key -> key.split("\\.")[1])
                .distinct()
                .forEach(id -> {
                    String firstName = properties.getProperty("trainee." + id + ".firstName");
                    String lastName = properties.getProperty("trainee." + id + ".lastName");
                    String dateOfBirthStr = properties.getProperty("trainee." + id + ".dateOfBirth");
                    String address = properties.getProperty("trainee." + id + ".address");
                    boolean isActive = Boolean.parseBoolean(properties.getProperty("trainee." + id + ".active"));

                    try {
                        Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirthStr);
                        Trainee trainee = new Trainee(firstName, lastName, null, null, isActive, dateOfBirth, address);
                        traineeStore.put(trainee.getId(), trainee);
                        logger.info("Loaded trainee: {} {}", firstName, lastName);
                    } catch (ParseException e) {
                        logger.error("Failed to parse date of birth for trainee with id: {}", id, e);
                    }
                });
    }

    private void loadTrainers(Properties properties) {
        properties.stringPropertyNames().stream()
                .filter(key -> key.startsWith("trainer."))
                .map(key -> key.split("\\.")[1])
                .distinct()
                .forEach(id -> {
                    String firstName = properties.getProperty("trainer." + id + ".firstName");
                    String lastName = properties.getProperty("trainer." + id + ".lastName");
                    String specialization = properties.getProperty("trainer." + id + ".specialization");
                    boolean isActive = Boolean.parseBoolean(properties.getProperty("trainer." + id + ".active"));

                    Trainer trainer = new Trainer(firstName, lastName, null, null, isActive, specialization);
                    trainerStore.put(trainer.getId(), trainer);
                    logger.info("Loaded trainer: {} {}", firstName, lastName);
                });
    }

    public Map<Long, Trainee> getTraineeStore() {
        return traineeStore;
    }

    public Map<Long, Trainer> getTrainerStore() {
        return trainerStore;
    }

    public Map<Long, Training> getTrainingStore() {
        return trainingStore;
    }
}