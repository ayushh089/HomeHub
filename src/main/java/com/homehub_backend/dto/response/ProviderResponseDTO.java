package com.homehub_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderResponseDTO {
    private String response; // "ACCEPTED", "REJECTED", etc.
    private String totalCost;
    private String notes;
    private LocalDateTime respondedAt;
}