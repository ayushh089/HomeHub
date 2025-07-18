package com.homehub_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class PayoutRequestDTO {
    @NotNull
    private UUID providerId;

    @NotNull
    @Positive
    private String amount;

    private String currency = "INR";
    private String notes;

    // Getters and setters
}