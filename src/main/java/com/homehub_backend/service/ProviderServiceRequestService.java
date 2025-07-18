package com.homehub_backend.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.homehub_backend.dao.*;
import com.homehub_backend.dto.request.ProviderResponseRequestDTO;
import com.homehub_backend.dto.response.ProviderResponseDTO;
import com.homehub_backend.dto.response.RequestStatusHistoryDTO;
import com.homehub_backend.dto.response.ServiceRequestResponseDTO;
import com.homehub_backend.dto.response.RequestSummaryDTO;
import com.homehub_backend.entity.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private RequestStatusHistoryRepository requestStatusHistoryRepository;

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

    public Page<ServiceRequestResponseDTO> getAssignedRequestsForProvider(UUID providerId,
            String status,
            Pageable pageable) {

        List<ServiceRequest.RequestStatus> assignedStatuses;

        if (status != null && !status.isEmpty()) {
            assignedStatuses = Arrays.asList(ServiceRequest.RequestStatus.valueOf(status));
        } else {
            assignedStatuses = Arrays.asList(
                    ServiceRequest.RequestStatus.SUBMITTED,
                    ServiceRequest.RequestStatus.REJECTED,
                    ServiceRequest.RequestStatus.PROVIDER_REVIEW,
                    ServiceRequest.RequestStatus.EXPIRED,
                    ServiceRequest.RequestStatus.OUT_FOR_SERVICE,

                    ServiceRequest.RequestStatus.QUOTED,
                    ServiceRequest.RequestStatus.SCHEDULED,
                    ServiceRequest.RequestStatus.IN_PROGRESS,
                    ServiceRequest.RequestStatus.COMPLETED);
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

        List<RequestStatusHistory> statusHistory = requestStatusHistoryRepository
                .findByServiceRequestIdOrderByCreatedAtDesc(request.getId());

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
                .residentName(resident != null ? resident.getFirstName() + " " + resident.getLastName() : "Unknown")
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

                .statusHistory(statusHistory.stream()
                        .map(history -> RequestStatusHistoryDTO.builder()
                                .fromStatus(String.valueOf(history.getFromStatus()))
                                .toStatus(history.getToStatus().toString())
                                .changedByType(history.getChangedByType().toString())
                                .reason(history.getReason())
                                .createdAt(history.getCreatedAt())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ProviderResponseDTO respondToServiceRequest(UUID requestId, UUID providerId,
            ProviderResponseRequestDTO responseDTO) throws BadRequestException {
        ServiceRequest request = serviceRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Service request not found"));

        if (request.getProviderId() == null || !request.getProviderId().equals(providerId)) {
            throw new BadRequestException("Provider is not assigned to this request");
        }

        ProviderResponse.ResponseType responseType;
        try {
            responseType = ProviderResponse.ResponseType.valueOf(responseDTO.getResponse().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid response type");
        }

        if (responseType == ProviderResponse.ResponseType.QUOTED &&
                (responseDTO.getTotalCost() == null || responseDTO.getTotalCost()==null)) {
            throw new BadRequestException("Total cost is required for quoted response");
        }

        ProviderResponse existingResponse = providerResponseRepository
                .findByServiceRequestIdAndProviderId(requestId, providerId);

        ProviderResponse response;
        if (existingResponse != null) {
            // Update existing response
            response = existingResponse;
            response.setResponse(responseType);
            response.setTotalCost(String.valueOf(responseDTO.getTotalCost()));
            response.setNotes(responseDTO.getNotes());
            request.setFinalCost(responseDTO.getTotalCost());
        } else {
            // Create new response
            response = new ProviderResponse();
            response.setServiceRequest(request);
            response.setProviderId(providerId);
            response.setResponse(responseType);
            response.setTotalCost(String.valueOf(responseDTO.getTotalCost()));
            response.setNotes(responseDTO.getNotes());
        }

        // Save the response
        response = providerResponseRepository.save(response);

        // Update the service request status based on response
        updateRequestStatus(request, responseType, response);

        return convertToProviderResponseDTO(response);
    }

    private void updateRequestStatus(ServiceRequest request,
            ProviderResponse.ResponseType responseType,
            ProviderResponse response) {
        // Store the old status before updating
        ServiceRequest.RequestStatus oldStatus = request.getStatus();
        ServiceRequest.RequestStatus newStatus = oldStatus; // default to same status

        switch (responseType) {
            case ACCEPTED:
                newStatus = ServiceRequest.RequestStatus.SCHEDULED;
                break;
            case REJECTED:
                newStatus = ServiceRequest.RequestStatus.REJECTED;
                break;
            case QUOTED:
                newStatus = ServiceRequest.RequestStatus.QUOTED;
                if (response.getTotalCost() != null) {
                    try {
                        request.setFinalCost(new BigDecimal(response.getTotalCost()));
                    } catch (NumberFormatException e) {
                        // Handle invalid number format
                    }
                }
                break;
            case MODIFIED:
                // Status might remain the same or change based on business logic
                break;
            case COMPLETED:
                newStatus = ServiceRequest.RequestStatus.COMPLETED;
                break;
            case OUT_FOR_SERVICE:
                newStatus = ServiceRequest.RequestStatus.OUT_FOR_SERVICE;
                break;
        }

        // Only save history if status changed
        if (oldStatus != newStatus) {
            request.setStatus(newStatus);
            saveStatusHistory(request, oldStatus, newStatus, response.getProviderId(),
                    RequestStatusHistory.UserType.PROVIDER, null, response.getNotes());
        }

        serviceRequestRepository.save(request);
    }

    private void saveStatusHistory(ServiceRequest request,
            ServiceRequest.RequestStatus fromStatus,
            ServiceRequest.RequestStatus toStatus,
            UUID changedBy,
            RequestStatusHistory.UserType changedByType,
            String reason,
            String notes) {
        RequestStatusHistory history = new RequestStatusHistory();
        history.setServiceRequest(request);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setChangedBy(changedBy);
        history.setChangedByType(changedByType);
        history.setReason(reason);
        history.setNotes(notes);

        requestStatusHistoryRepository.save(history);
    }

    public ProviderResponseDTO getProviderResponseForRequest(UUID requestId, UUID providerId) {
        ProviderResponse response = providerResponseRepository.findByServiceRequestIdAndProviderId(requestId,
                providerId);

        return convertToProviderResponseDTO(response);
    }

    private ProviderResponseDTO convertToProviderResponseDTO(ProviderResponse response) {
        return ProviderResponseDTO.builder()
                .id(response.getId())
                .requestId(response.getServiceRequest().getId())
                .providerId(response.getProviderId())
                .response(response.getResponse().toString())
                .totalCost(response.getTotalCost())
                .notes(response.getNotes())
                .respondedAt(response.getRespondedAt())
                .build();
    }

}