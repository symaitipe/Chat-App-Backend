package com.sahan.chatApp.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

}