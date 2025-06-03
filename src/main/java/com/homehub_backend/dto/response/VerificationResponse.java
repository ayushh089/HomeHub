package com.homehub_backend.dto.response;

import com.homehub_backend.dto.UserRole;
import lombok.Data;

@Data
public class VerificationResponse {
    private boolean success;
    private String message;
    private String token;
    private UserRole role;
    private String nextStep;

    public static VerificationResponse success(String token, UserRole role) {
        VerificationResponse response = new VerificationResponse();
        response.success = true;
        response.message = "Email verified successfully";
        response.token = token;
        response.role = role;
        response.nextStep = "COMPLETE_PROFILE";
        return response;
    }

    public static VerificationResponse error(String message) {
        VerificationResponse response = new VerificationResponse();
        response.success = false;
        response.message = message;
        return response;
    }
}