package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ServiceProviderSocietyResponse {
    private UUID id;
    private Boolean isPreferred;
    private String approvalStatus;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private UUID serviceProviderId;
    private UUID societyId;
    private UUID approvedByUserId; // Optional – depends on if `user` is set
}
