package com.homehub_backend.dao;

import com.homehub_backend.entity.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, UUID> {
    List<ServiceRequest> findByResidentIdAndStatusIn(UUID residentId, List<ServiceRequest.RequestStatus> statuses);

    List<ServiceRequest> findByProviderIdAndStatusIn(UUID providerId, List<ServiceRequest.RequestStatus> statuses);

    List<ServiceRequest> findBySocietyIdAndCategoryIdAndStatusIn(
            UUID societyId, UUID categoryId, List<ServiceRequest.RequestStatus> statuses);

    List<ServiceRequest> findByStatusAndExpiresAtBefore(
            ServiceRequest.RequestStatus status, LocalDateTime expiresAt);

    List<ServiceRequest> findByStatusIn(List<ServiceRequest.RequestStatus> statuses);

    List<ServiceRequest> findByResidentId(UUID residentId);
    List<ServiceRequest> findByProviderId(UUID providerId);



}