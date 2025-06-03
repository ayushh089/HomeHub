package com.homehub_backend.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OtpRequest {

    private UUID userId;

    @NotBlank(message = "OTP code is required")
    @Size(min = 6, max = 6, message = "OTP must be of 6 characters")
    private String otpCode;

}