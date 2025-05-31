package com.homehub_backend.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private String email;
    private String phone;
    private String password;
    private String role; // RESIDENT, ADMIN, etc.

    private String firstName;
    private String lastName;
    private String apartmentNumber;
    private UUID societyId;
    private String emergencyContact;

}
