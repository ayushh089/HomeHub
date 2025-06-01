package com.homehub_backend.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ServiceProviderSocietyRequest {
    private UUID serviceProviderId;
    private UUID societyId;
    private Boolean isPreferred;
    private String approvalStatus; // "PENDING", "APPROVED", or "REJECTED"
    private UUID approvedByUserId; // Optional – depends on flow
}
