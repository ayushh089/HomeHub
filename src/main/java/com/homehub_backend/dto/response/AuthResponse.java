package com.homehub_backend.dto.response;

import com.homehub_backend.dto.UserDto;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private UserDto user;

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponse(boolean success, String message, String token, UserDto user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }
}
