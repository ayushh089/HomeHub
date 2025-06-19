package com.homehub_backend.service;


import com.homehub_backend.dao.*;
import com.homehub_backend.dto.response.ServiceRequestResponseDTO;
import com.homehub_backend.dto.response.RequestSummaryDTO;
import com.homehub_backend.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderServiceRequestService {

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private RequestMediaRepository requestMediaRepository;

    @Autowired
    private ProviderResponseRepository providerResponseRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    private ServiceCategoryRepository serviceCategoryRepository;
//
//    public Page<ServiceRequestResponseDTO> getAvailableRequestsForProvider(UUID providerId,
//                                                                           UUID categoryId,
//                                                                           UUID societyId,
//                                                                           String urgency,
//                                                                           Pageable pageable) {
//
//        ServiceProvider provider = serviceProviderRepository.findById(providerId)
//                .orElseThrow(() -> new RuntimeException("Provider not found"));
//
//
//
//        List<ServiceRequest> allRequests = serviceRequestRepository.findByStatusIn(ServiceRequest.RequestStatus.SUBMITTED);
//
//        // Filter by provider's service areas and categories
//        List<ServiceRequest> filteredRequests = allRequests.stream()
//                .filter(req -> req.getProviderId() == null || req.getProviderId().equals(providerId))
//                .filter(req -> categoryId == null || req.getCategoryId().equals(categoryId))
//                .filter(req -> societyId == null || req.getSocietyId().equals(societyId))
//                .filter(req -> urgency == null || req.getUrgency().toString().equals(urgency))
//                .filter(req -> !hasProviderAlreadyResponded(req.getId(), providerId))
//                .collect(Collectors.toList());
//
//        // Apply pagination
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), filteredRequests.size());
//
//        List<ServiceRequest> paginatedRequests = filteredRequests.subList(start, end);
//
//        List<ServiceRequestResponseDTO> responseDTOs = paginatedRequests.stream()
//                .map(this::convertToResponseDTO)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(responseDTOs, pageable, filteredRequests.size());
//    }
//
    public Page<ServiceRequestResponseDTO> getAssignedRequestsForProvider(UUID providerId,
                                                                          String status,
                                                                          Pageable pageable) {

        List<ServiceRequest.RequestStatus> assignedStatuses;

        if (status != null && !status.isEmpty()) {
            assignedStatuses = Arrays.asList(ServiceRequest.RequestStatus.valueOf(status));
        } else {
            assignedStatuses = Arrays.asList(
                    ServiceRequest.RequestStatus.QUOTED,
                    ServiceRequest.RequestStatus.SCHEDULED,
                    ServiceRequest.RequestStatus.IN_PROGRESS,
                    ServiceRequest.RequestStatus.COMPLETED
            );
        }

        List<ServiceRequest> requests = serviceRequestRepository.findByProviderIdAndStatusIn(
                providerId, assignedStatuses);

        // Sort according to pageable
        requests.sort((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()));

        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), requests.size());

        List<ServiceRequest> paginatedRequests = requests.subList(start, end);

        List<ServiceRequestResponseDTO> responseDTOs = paginatedRequests.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(responseDTOs, pageable, requests.size());
    }

    public ServiceRequestResponseDTO getRequestDetailsForProvider(UUID requestId, UUID providerId) {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Service request not found"));
        System.out.println(request);

        // Check if provider has access to this request
        if (request.getProviderId() != null && !request.getProviderId().equals(providerId)) {
            throw new RuntimeException("Access denied to this service request");
        }

        return convertToResponseDTO(request);
    }
//
//    public RequestSummaryDTO getProviderRequestSummary(UUID providerId) {
//
//
//        List<ServiceRequest.RequestStatus> assignedStatuses = Arrays.asList(
//                ServiceRequest.RequestStatus.QUOTED,
//                ServiceRequest.RequestStatus.SCHEDULED,
//                ServiceRequest.RequestStatus.IN_PROGRESS
//        );
//
//        List<ServiceRequest.RequestStatus> completedStatuses = Arrays.asList(
//                ServiceRequest.RequestStatus.COMPLETED
//        );
//
//        int availableCount = serviceRequestRepository.findByStatusIn(ServiceRequest.RequestStatus.SUBMITTED).size();
//        int assignedCount = serviceRequestRepository.findByProviderIdAndStatusIn(providerId, assignedStatuses).size();
//        int completedCount = serviceRequestRepository.findByProviderIdAndStatusIn(providerId, completedStatuses).size();
//
//        return RequestSummaryDTO.builder()
//                .availableRequests(availableCount)
//                .assignedRequests(assignedCount)
//                .completedRequests(completedCount)
//                .totalEarnings(calculateTotalEarnings(providerId))
//                .build();
//    }
//
//    private boolean hasProviderAlreadyResponded(UUID requestId, UUID providerId) {
//        return providerResponseRepository.existsByRequestIdAndProviderId(requestId, providerId);
//    }
//
    private ServiceRequestResponseDTO convertToResponseDTO(ServiceRequest request) {
        // Get resident details
        Resident resident = residentRepository.findById(request.getResidentId())
                .orElse(null);

        // Get society details
        Society society = societyRepository.findById(request.getSocietyId())
                .orElse(null);

        // Get category details
        ServiceCategory category = serviceCategoryRepository.findById(request.getCategoryId())
                .orElse(null);

        // Get media files
        List<RequestMedia> mediaFiles = requestMediaRepository.findByServiceRequestId(request.getId());

        return ServiceRequestResponseDTO.builder()
                .id(request.getId())
                .description(request.getDescription())
                .urgency(request.getUrgency().toString())
                .status(request.getStatus().toString())
                .preferredDate(request.getPreferredDate())
                .preferredTimeSlot(request.getPreferredTimeSlot())
                .locationDetails(request.getLocationDetails())
                .contactPhone(request.getContactPhone())
                .finalCost(request.getFinalCost())
                .paymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod().toString() : null)
                .paymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus().toString() : null)
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .expiresAt(request.getExpiresAt())
                .residentName(resident != null ? resident.getFirstName()+" "+ resident.getLastName() : "Unknown")
                .residentPhone(resident != null ? resident.getApartmentNumber() : null)
                .societyName(society != null ? society.getName() : "Unknown")
                .categoryName(category != null ? category.getName() : "Unknown")
                .mediaFiles(mediaFiles.stream()
                        .map(media -> ServiceRequestResponseDTO.MediaFileDTO.builder()
                                .id(media.getId())
                                .url(media.getUrl())
                                .filename(media.getFilename())
                                .mediaType(media.getMediaType().toString())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
//
//    private Double calculateTotalEarnings(UUID providerId) {
//        List<ServiceRequest> completedRequests = serviceRequestRepository
//                .findByProviderIdAndStatusIn(providerId,
//                        Arrays.asList(ServiceRequest.RequestStatus.COMPLETED));
//
//        return completedRequests.stream()
//                .filter(req -> req.getFinalCost() != null)
//                .mapToDouble(req -> req.getFinalCost().doubleValue())
//                .sum();
//    }
}