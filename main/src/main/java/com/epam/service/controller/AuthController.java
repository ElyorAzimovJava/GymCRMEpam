    package com.epam.service.controller;

    import com.epam.service.dto.ChangePasswordRequestDto;
import com.epam.service.dto.LoginRequestDto;
import com.epam.service.service.BruteForceProtector;
import com.epam.service.service.JwtBlacklistService;
import com.epam.service.service.UserService;
import com.epam.service.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BruteForceProtector bruteForceProtector;
    private final JwtBlacklistService jwtBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        if (bruteForceProtector.isBlocked(request.getUsername())) {
            return ResponseEntity.status(401).body("User is blocked");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            bruteForceProtector.loginSucceeded(request.getUsername());

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(jwt);
        } catch (BadCredentialsException e) {
            bruteForceProtector.loginFailed(request.getUsername());
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            jwtBlacklistService.blacklist(jwt);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequestDto request) {
        userService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}