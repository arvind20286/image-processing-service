package com.app.ImageProcessingService.service;

import com.app.ImageProcessingService.entities.User;
import com.app.ImageProcessingService.model.AuthInfo;
import com.app.ImageProcessingService.repos.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public String register(AuthInfo request) throws IllegalArgumentException{
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .build();
        repository.save(user);
        return jwtService.generateToken(user);
    }

    public String login(AuthInfo request) throws AuthenticationException{
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));
        if(authentication.isAuthenticated()){
            User user = repository.findByUsername(request.username).get();
            return jwtService.generateToken(user);
        }
        return "Wrong Username or Password";
    }

}
