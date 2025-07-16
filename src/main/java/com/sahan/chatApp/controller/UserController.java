package com.sahan.chatApp.controller;

import com.sahan.chatApp.DTO.LoginRequest;
import com.sahan.chatApp.DTO.RegistrationRequest;
import com.sahan.chatApp.entity.User;
import com.sahan.chatApp.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private CustomUserDetailsService userService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request, BindingResult result) {

        if (result.hasErrors()) {
            // Return first validation error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }

        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUsername(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));

        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }


    @PostMapping("/login")
    public ResponseEntity<?>   loginValidation(@RequestBody LoginRequest loginRequest){
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