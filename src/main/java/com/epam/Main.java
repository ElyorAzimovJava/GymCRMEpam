package com.epam;

import com.epam.service.config.AppConfig;
import com.epam.service.entity.*;
import com.epam.service.facade.TrainingFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TrainingFacade trainingFacade = context.getBean(TrainingFacade.class);


        System.out.println("Loaded Trainees:");
        trainingFacade.selectAllTrainees().forEach(t -> System.out.println(t.getUser().getFirstName() + " " + t.getUser().getLastName()));

        System.out.println("Loaded Trainers:");
        trainingFacade.selectAllTrainers().forEach(t -> System.out.println(t.getUser().getFirstName() + " " + t.getUser().getLastName()));

        // Create a trainee
        Trainee trainee = Trainee.builder()
                .user(User.builder()
                .firstName("John")
                .lastName("Doe")
                .isActive(true).build())
                .dateOfBirth(new Date())
                .address("123 com.epam.Main St")
                .build();
        trainee = trainingFacade.createTrainee(trainee);
        System.out.println("Created Trainee: " + trainee.getUser().getUsername());

        // Create a trainer
        Trainer trainer = Trainer.builder()
                .user(User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .isActive(true).build())
                .specialization(TrainingType.CARDIO)
                .build();
        trainer = trainingFacade.createTrainer(trainer);
        System.out.println("Created Trainer: " + trainer.getUser().getUsername());

        // Create a training
        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName("Java Training")
                .trainingType(TrainingType.CARDIO)
                .trainingDate(new Date())
                .trainingDuration(Duration.ofMinutes(90))
                .build();
        training = trainingFacade.createTraining(training);
        System.out.println("Created Training: " + training.getTrainingName());
    }
}