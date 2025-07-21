package com.sahan.chatApp.controller;

import com.sahan.chatApp.DTO.LoginRequest;
import com.sahan.chatApp.DTO.PhoneNumberRequest;
import com.sahan.chatApp.DTO.OtpVerificationRequest;
import com.sahan.chatApp.DTO.RegistrationRequest;
import com.sahan.chatApp.entity.User;
import com.sahan.chatApp.service.CustomUserDetailsService;
import com.sahan.chatApp.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // Step 1: Request OTP with phone number only
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody PhoneNumberRequest request) {
        String otp = otpService.createOtpForPhone(request.getPhoneNumber());
        // In real apps, send OTP via SMS here, do NOT return it in response
        System.out.println(otp);
        return ResponseEntity.ok("OTP sent to phone number");
    }

    // Step 2: Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerificationRequest request) {
        boolean valid = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());
        if (valid) {
            // Mark user verified or create user if not exist
            User user = userService.findByPhoneNumber(request.getPhoneNumber());
            if (user == null) {
                user = new User();
                user.setPhoneNumber(request.getPhoneNumber());
                user.setVerified(false);
            }
            user.setVerified(true);
            userService.saveUser(user);

            return ResponseEntity.ok("OTP verified, you may proceed with registration");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }

    // Step 3: Register user with username + password after OTP verified
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        User user = userService.findByPhoneNumber(request.getPhoneNumber());
        if (user == null || !user.isVerified()) {
            return ResponseEntity.badRequest().body("Phone number not verified");
        }
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        userService.saveUser(user);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> test(@RequestBody LoginRequest loginRequest) {
         Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        if(authentication.isAuthenticated()){
            return ResponseEntity.ok("Login successful");
        }else{
            return new ResponseEntity<>("User Not found", HttpStatusCode.valueOf(404));
        }

    }
}
