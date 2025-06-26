package com.homehub_backend.dto.request;

import com.homehub_backend.dto.response.ProviderResponseDTO;
import com.homehub_backend.dto.response.RequestMediaDTO;
import com.homehub_backend.dto.response.RequestStatusHistoryDTO;
import com.homehub_backend.entity.ServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResidentServiceRequestDTO {
    private UUID id;
    private UUID providerId;
    private UUID societyId;
    private UUID categoryId;
    private String description;
    private ServiceRequest.UrgencyLevel urgency;
    private LocalDate preferredDate;
    private String preferredTimeSlot;
    private String locationDetails;
    private String contactPhone;
    private ServiceRequest.RequestStatus status;
    private LocalDateTime createdAt;
    private List<RequestMediaDTO> media;
    private List<RequestStatusHistoryDTO> statusHistory;
    private ProviderResponseDTO providerResponse;
}
