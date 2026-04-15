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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import com.epam.service.service.JwtBlacklistService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private BruteForceProtector bruteForceProtector;

    @MockBean
    private JwtBlacklistService jwtBlacklistService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    @Test
    void testLogout() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("user");
        loginRequest.setPassword("password");

        UserDetails userDetails = new User("user", "password", new ArrayList<>());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(bruteForceProtector.isBlocked("user")).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwt_token");
        when(jwtUtil.getUsernameFromToken("jwt_token")).thenReturn("user");
        when(userService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtUtil.validateToken("jwt_token", userDetails)).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String jwt = result.getResponse().getContentAsString();

        mockMvc.perform(post("/auth/logout").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

        when(jwtBlacklistService.isBlacklisted(jwt)).thenReturn(true);

        mockMvc.perform(get("/trainees/user")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testChangePassword() throws Exception {
        ChangePasswordRequestDto request = new ChangePasswordRequestDto();
        request.setUsername("user");
        request.setOldPassword("old_password");
        request.setNewPassword("new_password");

        doNothing().when(userService)
                .changePassword("user", "old_password", "new_password");

        UserDetails userDetails = new User("user", "password", new ArrayList<>());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(bruteForceProtector.isBlocked("user")).thenReturn(false);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwt_token");
        when(jwtUtil.getUsernameFromToken("jwt_token")).thenReturn("user");
        when(userService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtUtil.validateToken("jwt_token", userDetails)).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequestDto("user", "password"))))
                .andExpect(status().isOk())
                .andReturn();

        String jwt = result.getResponse().getContentAsString();

        mockMvc.perform(put("/auth/password").header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).changePassword("user", "old_password", "new_password");
    }
}