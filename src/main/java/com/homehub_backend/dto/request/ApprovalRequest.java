package com.homehub_backend.dto.request;

import com.homehub_backend.entity.ServiceProviderSociety;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest {
    @NotNull
    private ServiceProviderSociety.ApprovalStatus approvalStatus;

    private String rejectionReason; // Optional field for rejection reason
}