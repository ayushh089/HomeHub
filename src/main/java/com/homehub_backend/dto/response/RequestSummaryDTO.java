package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestSummaryDTO {
    private int availableRequests;
    private int assignedRequests;
    private int completedRequests;
    private double totalEarnings;
}