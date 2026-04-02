package com.epam.service.controller;

import com.epam.service.dto.ActivationRequestDto;
import com.epam.service.dto.RegistrationResponseDto;
import com.epam.service.dto.TraineeProfileResponseDto;
import com.epam.service.dto.TraineeProfileUpdateRequestDto;
import com.epam.service.dto.TraineeRegistrationRequestDto;
import com.epam.service.dto.TrainerDto;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.User;
import com.epam.service.service.TraineeService;
import com.epam.service.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerTrainee(@RequestBody TraineeRegistrationRequestDto request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.setAddress(request.getAddress());

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        // TODO:
        //  What is the use case for returning the password in the response?
        RegistrationResponseDto response = new RegistrationResponseDto(
                createdTrainee.getUser().getUsername(),
                createdTrainee.getUser().getPassword()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> updateTraineeStatus(@PathVariable("username") String username, @RequestBody ActivationRequestDto request) {
        if (request.isActive()) {
            traineeService.activateTrainee(username);
        } else {
            traineeService.deactivateTrainee(username);
        }
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerDto>> updateTraineeTrainers(@PathVariable("username") String username, @RequestBody List<String> trainerUsernames) {
        Trainee trainee = traineeService.selectTraineeByUsername(username);
        List<Trainer> trainers = trainerUsernames.stream()
                .map(trainerService::selectTrainerByUsername)
                .collect(Collectors.toList());

        trainee.setTrainers(trainers);
        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        List<TrainerDto> response = updatedTrainee.getTrainers().stream().map(trainer -> {
            TrainerDto trainerDto = new TrainerDto();
            trainerDto.setUsername(trainer.getUser().getUsername());
            trainerDto.setFirstName(trainer.getUser().getFirstName());
            trainerDto.setLastName(trainer.getUser().getLastName());
            trainerDto.setSpecialization(trainer.getSpecialization());
            return trainerDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDto> getTraineeProfile(@PathVariable("username") String username) {
        Trainee trainee = traineeService.selectTraineeByUsername(username);

        TraineeProfileResponseDto response = new TraineeProfileResponseDto();
        response.setFirstName(trainee.getUser().getFirstName());
        response.setLastName(trainee.getUser().getLastName());
        response.setDateOfBirth(trainee.getDateOfBirth());
        response.setAddress(trainee.getAddress());
        response.setActive(trainee.getUser().isActive());
        response.setTrainers(trainee.getTrainers().stream().map(trainer -> {
            TrainerDto trainerDto = new TrainerDto();
            trainerDto.setUsername(trainer.getUser().getUsername());
            trainerDto.setFirstName(trainer.getUser().getFirstName());
            trainerDto.setLastName(trainer.getUser().getLastName());
            trainerDto.setSpecialization(trainer.getSpecialization());
            return trainerDto;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDto> updateTraineeProfile(@PathVariable("username") String username, @RequestBody TraineeProfileUpdateRequestDto request) {
        Trainee traineeToUpdate = traineeService.selectTraineeByUsername(username);
        traineeToUpdate.getUser().setFirstName(request.getFirstName());
        traineeToUpdate.getUser().setLastName(request.getLastName());
        traineeToUpdate.setDateOfBirth(request.getDateOfBirth());
        traineeToUpdate.setAddress(request.getAddress());
        traineeToUpdate.getUser().setActive(request.isActive());

        Trainee updatedTrainee = traineeService.updateTrainee(traineeToUpdate);

        TraineeProfileResponseDto response = new TraineeProfileResponseDto();
        response.setFirstName(updatedTrainee.getUser().getFirstName());
        response.setLastName(updatedTrainee.getUser().getLastName());
        response.setDateOfBirth(updatedTrainee.getDateOfBirth());
        response.setAddress(updatedTrainee.getAddress());
        response.setActive(updatedTrainee.getUser().isActive());
        response.setTrainers(updatedTrainee.getTrainers().stream().map(trainer -> {
            TrainerDto trainerDto = new TrainerDto();
            trainerDto.setUsername(trainer.getUser().getUsername());
            trainerDto.setFirstName(trainer.getUser().getFirstName());
            trainerDto.setLastName(trainer.getUser().getLastName());
            trainerDto.setSpecialization(trainer.getSpecialization());
            return trainerDto;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainers/not-assigned")
    public ResponseEntity<List<TrainerDto>> getNotAssignedTrainers(@PathVariable("username") String username) {
        List<Trainer> notAssignedTrainers = trainerService.getUnassignedTrainers(username);
        List<TrainerDto> response = notAssignedTrainers.stream().map(trainer -> {
            TrainerDto trainerDto = new TrainerDto();
            trainerDto.setUsername(trainer.getUser().getUsername());
            trainerDto.setFirstName(trainer.getUser().getFirstName());
            trainerDto.setLastName(trainer.getUser().getLastName());
            trainerDto.setSpecialization(trainer.getSpecialization());
            return trainerDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}