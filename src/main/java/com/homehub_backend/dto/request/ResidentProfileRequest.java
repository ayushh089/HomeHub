package com.homehub_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class ResidentProfileRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2-100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2-100 characters")
    private String lastName;

    @NotBlank(message = "Apartment number is required")
    private String apartmentNumber;

    @NotNull(message = "Society selection is required")
    private UUID societyId;

    private String emergencyContact; // Optional
}
