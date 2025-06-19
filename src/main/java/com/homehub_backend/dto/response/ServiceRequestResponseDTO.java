
package com.homehub_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ServiceRequestResponseDTO {
    private UUID id;
    private String description;
    private String urgency;
    private String status;
    private LocalDate preferredDate;
    private String preferredTimeSlot;
    private String locationDetails;
    private String contactPhone;
    private BigDecimal finalCost;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;

    // Resident details
    private String residentName;
    private String residentPhone;

    // Society details
    private String societyName;

    // Category details
    private String categoryName;

    // Media files
    private List<MediaFileDTO> mediaFiles;

    @Data
    @Builder
    public static class MediaFileDTO {
        private UUID id;
        private String url;
        private String filename;
        private String mediaType;
    }
}