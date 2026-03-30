package com.epam.service.controller;

import com.epam.service.dto.ChangePasswordRequestDto;
import com.epam.service.dto.LoginRequestDto;
import com.epam.service.service.BruteForceProtector;
import com.epam.service.service.UserService;
import com.epam.service.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BruteForceProtector bruteForceProtector;

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

        UserDetails userDetails = new User("user", "password", new ArrayList<>());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(bruteForceProtector.isBlocked("user")).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwt_token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt_token"));

        verify(bruteForceProtector).loginSucceeded("user");
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("user");
        request.setPassword("wrong_password");

        when(bruteForceProtector.isBlocked("user")).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(bruteForceProtector).loginFailed("user");
    }

    @Test
    void testLoginBlocked() throws Exception {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("user");
        request.setPassword("password");

        when(bruteForceProtector.isBlocked("user")).thenReturn(true);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(authenticationManager, never()).authenticate(any());
    }


    // TODO:
    //  Have you tried to test the following scenario:
    //  1. User logs in successfully and receives a JWT
    //  2. User calls logout
    //  3. User tries to access a protected resource with the same JWT
    //  To test that I would recommend to change this class a bit, it should not use mockMvc.standalone,
    //  but rather @SpringBootTest so that the whole security configuration and a jwt filter are loaded
    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk());
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