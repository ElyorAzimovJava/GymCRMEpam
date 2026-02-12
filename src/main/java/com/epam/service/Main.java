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
        Trainee trainee = new Trainee("John", "Doe", null, null, true, new Date(), "123 Main St");
        trainee = trainingFacade.createTrainee(trainee);
        System.out.println("Created Trainee: " + trainee.getUsername());

        // Create a trainer
        Trainer trainer = new Trainer("Jane", "Doe", null, null, true, "Java");
        trainer = trainingFacade.createTrainer(trainer);
        System.out.println("Created Trainer: " + trainer.getUsername());

        // Create a training
        TrainingType trainingType = new TrainingType("Java Development");
        Training training = new Training(trainee.getId(), trainer.getId(), "Java Training", trainingType, new Date(), 60);
        training = trainingFacade.createTraining(training);
        System.out.println("Created Training: " + training.getTrainingName());
    }
}