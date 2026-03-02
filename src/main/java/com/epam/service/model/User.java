package com.epam.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
// TODO:
//  Yes, @Inheritance will create a hierarchy in the database, but it also creates some limitations.
//  For example, you won't be able to have a user who is both a trainer and trainee.
//  Let's do it as the task requires which is @OneToOne between User and Trainee/Trainer
//  As we want to have Trainer.id separate from User.id
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // TODO:
    //  If your entity has id = 0, is it a new entity or an existing one?
    //  Consider using wrapper here
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    // TODO:
    //  This one is even more tricky. If the column default value was false I would say primitive can be acceptable.
    //  But I assume when we create a new user we want it to be active by default.
    //  Problems with current implementation:
    //  Case 1: Developer forgets to initialize this field when creating a new user.
    //  Case 2: PATCH-style update with mapping from a DTO that may omit `active` (may become relevant later in the course)
    //  Both can result in accidental overwrite of the `active` field to false
    //  Possible solutions:
    //     - Initialize explicitly: `private boolean active = true;`
    //     - Or use `Boolean` to distinguish "not provided" (null) from explicit false.
    private boolean isActive;

}