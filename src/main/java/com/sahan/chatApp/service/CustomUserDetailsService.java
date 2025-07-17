package com.sahan.chatApp.service;

import com.sahan.chatApp.entity.User;
import com.sahan.chatApp.principal.UserDetailsImplementation;
import com.sahan.chatApp.repository.UserRepo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    // -------------------------- Login validation --------------------------------------------------
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new UserDetailsImplementation(user);

    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepo.findById(phoneNumber).orElse(null);
    }
}
