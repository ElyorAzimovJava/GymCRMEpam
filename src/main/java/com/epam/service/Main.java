package com.epam.service;

import com.epam.service.config.AppConfig;
import com.epam.service.facade.TrainingFacade;
import com.epam.service.model.Trainee;
import com.epam.service.model.Trainer;
import com.epam.service.model.Training;
import com.epam.service.model.TrainingType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);

        // Print loaded data
        System.out.println("Loaded Trainees:");
        trainingFacade.selectAllTrainees().forEach(t -> System.out.println(t.getFirstName() + " " + t.getLastName()));

        System.out.println("Loaded Trainers:");
        trainingFacade.selectAllTrainers().forEach(t -> System.out.println(t.getFirstName() + " " + t.getLastName()));

        // Create a trainee
        Trainee trainee = Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .dateOfBirth(new Date())
                .address("123 Main St")
                .build();
        trainee = trainingFacade.createTrainee(trainee);
        System.out.println("Created Trainee: " + trainee.getUsername());

        // Create a trainer
        Trainer trainer = Trainer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .isActive(true)
                .specialization("Java")
                .build();
        trainer = trainingFacade.createTrainer(trainer);
        System.out.println("Created Trainer: " + trainer.getUsername());

        // Create a training
        Training training = Training.builder()
                .traineeId(trainee.getId())
                .trainerId(trainer.getId())
                .trainingName("Java Training")
                .trainingType(TrainingType.CARDIO)
                .trainingDate(new Date())
                .trainingDuration(60)
                .build();
        training = trainingFacade.createTraining(training);
        System.out.println("Created Training: " + training.getTrainingName());
    }
}