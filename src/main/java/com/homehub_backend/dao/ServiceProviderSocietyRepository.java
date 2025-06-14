package com.homehub_backend.dao;

import com.homehub_backend.entity.ServiceProviderSociety;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceProviderSocietyRepository extends JpaRepository<ServiceProviderSociety, UUID> {


    List<ServiceProviderSociety> findBySocietyId(UUID societyId);

    List<ServiceProviderSociety> findBySocietyIdAndApprovalStatus(UUID societyId, ServiceProviderSociety.ApprovalStatus approvalStatus);

    List<ServiceProviderSociety> findByServiceProviderUserId(UUID serviceProviderId);

    List<ServiceProviderSociety> findByApprovalStatus(ServiceProviderSociety.ApprovalStatus approvalStatus);

    ServiceProviderSociety findByServiceProviderUserIdAndSocietyId(UUID serviceProviderId, UUID societyId);
}
