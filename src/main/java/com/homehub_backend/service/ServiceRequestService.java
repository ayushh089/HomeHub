package com.homehub_backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.homehub_backend.dao.*;
import com.homehub_backend.dto.request.ServiceRequestDTO;
import com.homehub_backend.entity.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {

    @Autowired
    ServiceRequestRepository serviceRequestRepository;

    @Autowired
    RequestStatusHistoryRepository statusHistoryRepository;


    @Autowired
    SocietyRepository societyRepository;

    @Autowired
    ServiceCategoryRepository serviceCategoryRepository;

    @Autowired
    ResidentRepository residentRepository;

    @Autowired
    RequestMediaRepository requestMediaRepository;

    @Autowired
    AmazonS3 amazonS3;

    @Autowired
    NotificationService notificationService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public ServiceRequest createAndSubmitRequest(ServiceRequestDTO requestDTO, List<MultipartFile> files, UUID residentId) {
        System.out.println(requestDTO);

        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + residentId));

        Society society = societyRepository.findById(requestDTO.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found with ID: " + requestDTO.getSocietyId()));
        ServiceCategory category = serviceCategoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + requestDTO.getCategoryId()));


        ServiceRequest request = ServiceRequest.builder()
                .residentId(residentId)
                .providerId(requestDTO.getProviderId())
                .societyId(requestDTO.getSocietyId())
                .categoryId(requestDTO.getCategoryId())
                .description(requestDTO.getDescription())
                .urgency(requestDTO.getUrgencyLevel())
                .preferredDate(requestDTO.getPreferredDate())
                .preferredTimeSlot(requestDTO.getPreferredTimeSlot())
                .locationDetails(requestDTO.getLocationDetails())
                .contactPhone(requestDTO.getContactPhone())
                .createdAt(LocalDateTime.now())
                .status(ServiceRequest.RequestStatus.SUBMITTED)
                .build();

        ServiceRequest savedRequest = serviceRequestRepository.save(request);

        if (files != null) {
            uploadAndSaveMediaFiles(files, savedRequest.getId(), request);
        }

        addStatusHistory(savedRequest, null, ServiceRequest.RequestStatus.SUBMITTED, residentId, RequestStatusHistory.UserType.RESIDENT, "Request Created");


        notificationService.notifyProvider(requestDTO.getProviderId(), "New service request from Resident " + resident.getFirstName() + " " + resident.getLastName());
        return savedRequest;
    }


    private void addStatusHistory(ServiceRequest request, ServiceRequest.RequestStatus fromStatus,
                                  ServiceRequest.RequestStatus toStatus, UUID changedById,
                                  RequestStatusHistory.UserType changedByType, String reason) {
        RequestStatusHistory history = new RequestStatusHistory();
        history.setServiceRequest(request);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setChangedBy(changedById);
        history.setChangedByType(changedByType);
        history.setReason(reason);

        statusHistoryRepository.save(history);
    }

    private void uploadAndSaveMediaFiles(List<MultipartFile> files, UUID requestId, ServiceRequest request) {
        if (amazonS3 == null) {
            throw new IllegalStateException("AmazonS3 client not initialized");
        }

        files.forEach(file -> {
            try {
                if (file.isEmpty()) {
                    return;
                }

                String fileKey = "requests/" + requestId + "/" + UUID.randomUUID() + "_" +
                        Objects.requireNonNull(file.getOriginalFilename());

                // Upload to S3 with metadata
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());

                amazonS3.putObject(new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata));

                // Create media record
                RequestMedia media = RequestMedia.builder()
                        .serviceRequest(request)
                        .url(fileKey)
                        .filename(file.getOriginalFilename())
                        .fileSize((int) file.getSize())
                        .mediaType(determineMediaType(file.getContentType()))
                        .mimeType(file.getContentType())
                        .uploadedBy(request.getResidentId())
                        .uploadedByType(RequestMedia.UserType.RESIDENT)
                        .createdAt(LocalDateTime.now())
                        .build();

                requestMediaRepository.save(media);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
            }
        });
    }

    private RequestMedia.MediaType determineMediaType(String mimeType) {
        if (mimeType.startsWith("image/")) return RequestMedia.MediaType.IMAGE;
        else if (mimeType.startsWith("video/")) return RequestMedia.MediaType.VIDEO;
        else if (mimeType.equals("application/pdf")) return RequestMedia.MediaType.DOCUMENT;
        return RequestMedia.MediaType.DOCUMENT; // default
    }


}