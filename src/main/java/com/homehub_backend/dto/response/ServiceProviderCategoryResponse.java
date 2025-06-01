package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ServiceProviderCategoryResponse {

    private UUID id;

    private Integer hourlyRate;

    private Integer minCharge;

    private Boolean isPrimary;

    private LocalDateTime createdAt;

    private UUID serviceProviderId;

    private UUID categoryId;
}
