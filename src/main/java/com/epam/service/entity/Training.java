package com.epam.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Duration;
// TODO:
//  Unused imports! Please review and clean the project from all redundant code.
//  IDE code inspection shows 23 unused imports and 29 unused declarations warnings
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
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
    //  please specify which units this field is in (hours, minutes, seconds, etc)
    private Duration trainingDuration;

}