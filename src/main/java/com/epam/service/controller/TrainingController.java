package com.epam.service.controller;

import com.epam.service.dto.TraineeTrainingResponseDto;
import com.epam.service.dto.TrainerTrainingResponseDto;
import com.epam.service.dto.TrainingRequestDto;
import com.epam.service.entity.Trainee;
import com.epam.service.entity.Trainer;
import com.epam.service.entity.Training;
import com.epam.service.entity.TrainingType;
import com.epam.service.service.TraineeService;
import com.epam.service.service.TrainerService;
import com.epam.service.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @PostMapping
    public ResponseEntity<Void> createTraining(@RequestBody TrainingRequestDto request) {
        Trainee trainee = traineeService.selectTraineeByUsername(request.getTraineeUsername());
        Trainer trainer = trainerService.selectTrainerByUsername(request.getTrainerUsername());

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setTrainingDuration(request.getTrainingDuration());
        training.setTrainingType(trainer.getSpecialization());

        trainingService.createTraining(training);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }     
    @GetMapping("/trainee/{username}")
    public ResponseEntity<List<TraineeTrainingResponseDto>> getTraineeTrainings(
            @PathVariable("username") String username,
            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,

            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,

            @RequestParam(value = "trainerName", required = false) String trainerName,

            @RequestParam(value = "trainingType", required = false) TrainingType trainingType) {

        List<Training> trainings = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        List<TraineeTrainingResponseDto> response = trainings.stream()
                .map(training -> new TraineeTrainingResponseDto(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType(),
                        training.getTrainingDuration(),
                        training.getTrainer().getUser().getFirstName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
        @GetMapping("/types")
    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
        return ResponseEntity.ok(Arrays.asList(TrainingType.values()));
    }

    @GetMapping("/trainer/{username}")
    public ResponseEntity<List<TrainerTrainingResponseDto>> getTrainerTrainings(
            @PathVariable("username") String username,

            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,

            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,

            @RequestParam(value = "traineeName", required = false) String traineeName,

            @RequestParam(value = "trainingType", required = false) TrainingType trainingType) {

        List<Training> trainings = trainingService.getTrainerTrainings(username, fromDate, toDate, traineeName, trainingType);

        List<TrainerTrainingResponseDto> response = trainings.stream()
                .map(training -> new TrainerTrainingResponseDto(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType(),
                        training.getTrainingDuration(),
                        training.getTrainee().getUser().getFirstName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
