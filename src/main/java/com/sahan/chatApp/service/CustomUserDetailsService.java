package com.sahan.chatApp.service;

import com.sahan.chatApp.entity.User;
import com.sahan.chatApp.principal.UserDetailsImplementation;
import com.sahan.chatApp.repository.UserRepo;
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

    // -------------------------- register --------------------------------------------------
    public User registerUser(User registerRequestedUser){
        return userRepo.save(registerRequestedUser);
    }
}
