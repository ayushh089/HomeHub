package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProviderResponseDTO {
    private UUID id;
    private UUID requestId;
    private UUID providerId;
    private String response;
    private String totalCost;
    private String notes;
    private LocalDateTime respondedAt;
}