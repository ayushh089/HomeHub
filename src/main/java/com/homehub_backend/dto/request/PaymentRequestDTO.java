package com.homehub_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Setter
@Getter
@Builder
public class PaymentRequestDTO {
    @NotNull
    private UUID requestId;

    @NotNull
    private UUID residentId;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String currency = "INR";

    // Getters and setters
}