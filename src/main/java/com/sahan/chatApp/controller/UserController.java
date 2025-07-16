package com.sahan.chatApp.controller;

import com.sahan.chatApp.model.User;
import com.sahan.chatApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User requestedUser) {
         User user = userService.registerUser(requestedUser);
         if(user!=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
         }else{
             return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
         }
    }
}