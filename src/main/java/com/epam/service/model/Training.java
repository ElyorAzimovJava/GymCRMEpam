// TODO:
//  Since we are using Hibernate and JPA it is conventional to name the package `entity`
//  instead of `model`, since these classes are persistence entities.
//  The `model` package name is typically used for DTOs or API models.
package com.epam.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Training implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(nullable = false)
    private String trainingName;

    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;
    @Column(nullable = false)
    private Date trainingDate;
    // TODO:
    //  Still not sure is it minutes, seconds, hours?
    //  Here is a good place for a comment to clarify for everyone who's reading your code
    //  Or rename the variable
    private int trainingDuration;

}