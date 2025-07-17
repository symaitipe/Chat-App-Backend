package com.sahan.chatApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;   // Used for login, adding contacts, etc.

    private String username;  // Can be null until the user sets it

    private String password;  // Can be null until the user sets it

    @Column
    private boolean isVerified=false;   // Mark after OTP verified
}

