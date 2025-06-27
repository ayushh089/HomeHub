package com.homehub_backend.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProviderResponseRequestDTO {
    private String response; // "ACCEPTED", "REJECTED", etc.
    private String totalCost; // Required for QUOTED response
    private String notes;
}