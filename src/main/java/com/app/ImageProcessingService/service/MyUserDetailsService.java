package com.app.ImageProcessingService.service;

import com.app.ImageProcessingService.repos.UserRepository;
import com.app.ImageProcessingService.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> obj = userRepo.findByUsername(username);
        if(obj.isEmpty()){
            throw new UsernameNotFoundException("User" + username + "not found");
        }
        return obj.get();
    }
}
