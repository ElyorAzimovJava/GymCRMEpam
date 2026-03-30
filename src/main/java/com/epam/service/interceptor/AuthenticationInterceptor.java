package com.epam.service.interceptor;

import com.epam.service.service.UserService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;


// TODO:
//  Do you still need Basic Auth if JWT filter is implemented?
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle( HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authHeader.substring("Basic ".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                String[] values = credentials.split(":", 2);

                if (values.length == 2) {
                    String username = values[0];
                    String password = values[1];

                    if (userService.checkLogin(username, password)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                log.warn("Authentication failed due to invalid credentials format or user not found");
            }
        }

        log.warn("Unauthorized access attempt to: {}", request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}