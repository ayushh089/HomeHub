package com.homehub_backend.dto.response;

import com.homehub_backend.dto.UserRole;
import lombok.Data;

@Data
public class SignupResponse {
    private boolean success;
    private String message;
    private String userId;
    private String email;
    private UserRole role;
    private String nextStep;

    public static SignupResponse success(String userId, UserRole role,String email) {
        SignupResponse response = new SignupResponse();
        response.success = true;
        response.message = "Signup successful. Please verify your email.";
        response.userId = userId;
        response.email=email;
        response.role = role;
        response.nextStep = "VERIFY_EMAIL";
        return response;
    }

    public static SignupResponse error(String message) {
        SignupResponse response = new SignupResponse();
        response.success = false;
        response.message = message;
        return response;
    }
}
