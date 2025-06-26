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
public class RequestStatusHistoryDTO {
    private String fromStatus;
    private String toStatus;
    private String changedByType;
    private String reason;
    private LocalDateTime createdAt;
}