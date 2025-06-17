package com.homehub_backend.dao;

import com.homehub_backend.entity.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, UUID> {
    List<ServiceRequest> findByResidentIdAndStatusIn(UUID residentId, List<ServiceRequest.RequestStatus> statuses);

    List<ServiceRequest> findByProviderIdAndStatusIn(UUID providerId, List<ServiceRequest.RequestStatus> statuses);

    List<ServiceRequest> findBySocietyIdAndCategoryIdAndStatusIn(
            UUID societyId, UUID categoryId, List<ServiceRequest.RequestStatus> statuses);

    List<ServiceRequest> findByStatusAndExpiresAtBefore(
            ServiceRequest.RequestStatus status, LocalDateTime expiresAt);

    @Query("SELECT sr FROM ServiceRequest sr WHERE " +
            "(:residentId IS NULL OR sr.residentId = :residentId) AND " +
            "(:providerId IS NULL OR sr.providerId = :providerId) AND " +
            "(:societyId IS NULL OR sr.societyId = :societyId) AND " +
            "(:categoryId IS NULL OR sr.categoryId = :categoryId) AND " +
            "(:status IS NULL OR sr.status = :status)")
    Page<ServiceRequest> findWithFilters(
            @Param("residentId") UUID residentId,
            @Param("providerId") UUID providerId,
            @Param("societyId") UUID societyId,
            @Param("categoryId") UUID categoryId,
            @Param("status") ServiceRequest.RequestStatus status,
            Pageable pageable);
}