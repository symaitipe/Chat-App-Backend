package com.sahan.chatApp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PhoneNumberRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+94|0)?[0-9]{9}$", message = "Invalid Sri Lankan phone number")
    private String phoneNumber;
}
