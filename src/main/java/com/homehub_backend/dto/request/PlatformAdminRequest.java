package com.homehub_backend.dto.request;


import com.homehub_backend.entity.Users;
import lombok.Data;

@Data
public class PlatformAdminRequest {

    private String email;
    private String phone;
    private String password;
    private String role;
    private Users user;

    private String firstName;
    private String lastName;
}
