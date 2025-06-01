package com.homehub_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;

@Data
public class ServiceProviderCategoryRequest {

    @NotNull(message = "Service Provider ID is required")
    private UUID serviceProviderId;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @PositiveOrZero(message = "Hourly rate must be zero or positive")
    private Integer hourlyRate;

    @PositiveOrZero(message = "Minimum charge must be zero or positive")
    private Integer minCharge;

    private Boolean isPrimary = false;
}
