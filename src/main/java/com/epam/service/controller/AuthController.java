    package com.epam.service.controller;

    import com.epam.service.dto.ChangePasswordRequestDto;
    import com.epam.service.dto.LoginRequestDto;
    import com.epam.service.service.UserService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public class AuthController {

        private final UserService userService;

        @PostMapping("/login")
        public ResponseEntity<Void> login(@RequestBody LoginRequestDto request) {
            if (userService.checkCredentials(request.getUsername(), request.getPassword())) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(401).build();
        }

        @PutMapping("/password")
        public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequestDto request) {
            userService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok().build();
        }
    }