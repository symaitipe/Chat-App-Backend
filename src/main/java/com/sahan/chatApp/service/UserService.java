package com.sahan.chatApp.service;


import com.sahan.chatApp.model.User;
import com.sahan.chatApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public User registerUser(User requestedUser){
        return userRepo.save(requestedUser);
    }
}
