package com.sahan.chatApp.controller;

import com.sahan.chatApp.DTO.LoginRequest;
import com.sahan.chatApp.entity.User;
import com.sahan.chatApp.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private CustomUserDetailsService userService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User requestedUser) {
        System.out.println("----------------- Login called ---------------------------------------");
         requestedUser.setPassword(encoder.encode(requestedUser.getPassword()));
         User user = userService.registerUser(requestedUser);

         if(user!=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
         }else{
             System.out.println("Searched User Not found...........");
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         }
    }

    @PostMapping("/login")
    public ResponseEntity<?>   loginValidation(@RequestBody LoginRequest loginRequest){
        System.out.println("----------------- Login called ---------------------------------------");
         UserDetails userDetails = userService.loadUserByUsername(loginRequest.getUsername());

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        if (!encoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password!");
        }

        return ResponseEntity.ok("Login Successful!");
    }

}