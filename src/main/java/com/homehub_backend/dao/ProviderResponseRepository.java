package com.homehub_backend.dao;

import com.homehub_backend.entity.ProviderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProviderResponseRepository extends JpaRepository<ProviderResponse, UUID> {

    List<ProviderResponse> findByServiceRequestId(UUID requestId);

    // Use the relationship property name with providerId
    ProviderResponse findByServiceRequestIdAndProviderId(UUID requestId, UUID providerId);

}
