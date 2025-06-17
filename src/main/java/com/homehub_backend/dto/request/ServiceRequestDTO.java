package com.homehub_backend.dto.request;

import com.homehub_backend.entity.ServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDTO {


    @NotBlank
    private String description;

    @NotNull
    private ServiceRequest.UrgencyLevel urgencyLevel;

    private LocalDate preferredDate;
    private String preferredTimeSlot;
    private String locationDetails;
    private String contactPhone;

    @NotNull
    private UUID societyId;

    @NotNull
    private UUID categoryId;

    @NotNull
    private UUID providerId;

}