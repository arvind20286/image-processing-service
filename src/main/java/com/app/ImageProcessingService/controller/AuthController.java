package com.app.ImageProcessingService.controller;

import com.app.ImageProcessingService.model.AuthInfo;
import com.app.ImageProcessingService.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173, https://imagery.rishmi5h.com")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthInfo request) {
        log.info("Received registration request for user: {}", request.getUsername());
//        return authService.register(request);
        try {
            String jwt = authService.register(request);
            if (jwt.chars().filter(ch -> ch == '.').count() != 2) {
                log.error("Invalid JWT token generated for user: {}", request.getUsername());
                throw new IllegalArgumentException("Invalid JWT token");
            }
            log.info("Successfully registered user: {}", request.getUsername());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwt);
            return ResponseEntity.ok().headers(headers).body(jwt);
        } catch (IllegalArgumentException e) {
            log.error("Registration failed for user: {}", request.getUsername(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthInfo request){
        log.info("Received login request for user: {}", request.getUsername());
        try {
            String jwt = authService.login(request);
            if (jwt.chars().filter(ch -> ch == '.').count() != 2) {
                log.error("Invalid JWT token generated for user: {}", request.getUsername());
                throw new IllegalArgumentException("Invalid JWT token");
            }
            log.info("Successfully logged in user: {}", request.getUsername());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + jwt);
            return ResponseEntity.ok().headers(headers).body(jwt);
        } catch (IllegalArgumentException e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (AuthenticationException e){
            log.error("Authentication Failed. Wrong Username and Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            log.error("Unexpected error during login for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }

    }

}
