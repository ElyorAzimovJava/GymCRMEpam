package com.epam.service.controller;

import com.epam.service.dto.ChangePasswordRequestDto;
import com.epam.service.dto.LoginRequestDto;
import com.epam.service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("user");
        request.setPassword("password");

        when(userService.checkCredentials("user", "password")).thenReturn(true);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).checkCredentials("user", "password");
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("user");
        request.setPassword("wrong_password");

        when(userService.checkCredentials("user", "wrong_password")).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(userService).checkCredentials("user", "wrong_password");
    }

    @Test
    void testChangePassword() throws Exception {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setUsername("user");
        request.setOldPassword("old_password");
        request.setNewPassword("new_password");

        doNothing().when(userService)
                .changePassword("user", "old_password", "new_password");

        mockMvc.perform(put("/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).changePassword("user", "old_password", "new_password");
    }
}