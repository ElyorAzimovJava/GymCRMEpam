package com.epam.service.controller;

import com.epam.service.dto.ChangePasswordRequestDto;
import com.epam.service.dto.LoginRequestDto;
import com.epam.service.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Test
    void testLoginSuccess() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("John.Doe");
        request.setPassword("password");

        when(userService.checkCredentials("John.Doe", "password")).thenReturn(true);

        ResponseEntity<Void> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(userService, times(1))
                .checkCredentials("John.Doe", "password");
    }

    @Test
    void testLoginUnauthorized() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("John.Doe");
        request.setPassword("wrongPassword");

        when(userService.checkCredentials("John.Doe", "wrongPassword")).thenReturn(false);

        ResponseEntity<Void> response = authController.login(request);

        assertEquals(401, response.getStatusCodeValue());
        verify(userService, times(1))
                .checkCredentials("John.Doe", "wrongPassword");
    }

    @Test
    void testChangePassword() {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setUsername("John.Doe");
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");

        doNothing().when(userService)
                .changePassword("John.Doe", "oldPassword", "newPassword");

        ResponseEntity<Void> response = authController.changePassword(request);

        assertEquals(200, response.getStatusCodeValue());

        verify(userService, times(1))
                .changePassword("John.Doe", "oldPassword", "newPassword");
    }
}