package com.sahan.chatApp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OtpVerificationRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+94|0)?[0-9]{9}$", message = "Invalid Sri Lankan phone number")
    private String phoneNumber;

    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "^[0-9]{4,6}$", message = "Invalid OTP format")
    private String otp;
}
