package com.epam.service.controller;

import com.epam.service.dto.ActivationRequestDto;
import com.epam.service.dto.RegistrationResponseDto;
import com.epam.service.dto.TrainerProfileResponseDto;
import com.epam.service.dto.TraineeDto;
import com.epam.service.dto.TrainerProfileUpdateRequestDto;
import com.epam.service.dto.TrainerRegistrationRequestDto;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.User;
import com.epam.service.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerTrainer(@RequestBody TrainerRegistrationRequestDto request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(request.getSpecialization());

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        RegistrationResponseDto response = new RegistrationResponseDto(
                createdTrainer.getUser().getUsername(),
                createdTrainer.getUser().getPassword()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> updateTrainerStatus(@PathVariable String username, @RequestBody ActivationRequestDto request) {
        if (request.isActive()) {
            trainerService.activateTrainer(username);
        } else {
            trainerService.deactivateTrainer(username);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDto> getTrainerProfile(@PathVariable String username) {
        Trainer trainer = trainerService.selectTrainerByUsername(username);

        TrainerProfileResponseDto response = new TrainerProfileResponseDto();
        response.setFirstName(trainer.getUser().getFirstName());
        response.setLastName(trainer.getUser().getLastName());
        response.setSpecialization(trainer.getSpecialization());
        response.setActive(trainer.getUser().isActive());
        response.setTrainees(trainer.getTrainees().stream().map(trainee -> {
            TraineeDto traineeDto = new TraineeDto();
            traineeDto.setUsername(trainee.getUser().getUsername());
            traineeDto.setFirstName(trainee.getUser().getFirstName());
            traineeDto.setLastName(trainee.getUser().getLastName());
            return traineeDto;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDto> updateTrainerProfile(@PathVariable String username, @RequestBody TrainerProfileUpdateRequestDto request) {
        Trainer trainerToUpdate = trainerService.selectTrainerByUsername(username);
        trainerToUpdate.getUser().setFirstName(request.getFirstName());
        trainerToUpdate.getUser().setLastName(request.getLastName());
        trainerToUpdate.setSpecialization(request.getSpecialization());
        trainerToUpdate.getUser().setActive(request.isActive());

        Trainer updatedTrainer = trainerService.updateTrainer(trainerToUpdate);

        TrainerProfileResponseDto response = new TrainerProfileResponseDto();
        response.setFirstName(updatedTrainer.getUser().getFirstName());
        response.setLastName(updatedTrainer.getUser().getLastName());
        response.setSpecialization(updatedTrainer.getSpecialization());
        response.setActive(updatedTrainer.getUser().isActive());
        response.setTrainees(updatedTrainer.getTrainees().stream().map(trainee -> {
            TraineeDto traineeDto = new TraineeDto();
            traineeDto.setUsername(trainee.getUser().getUsername());
            traineeDto.setFirstName(trainee.getUser().getFirstName());
            traineeDto.setLastName(trainee.getUser().getLastName());
            return traineeDto;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}