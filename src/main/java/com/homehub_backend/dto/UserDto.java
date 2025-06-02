package com.homehub_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data

@Builder
@AllArgsConstructor
@NoArgsConstructor
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
