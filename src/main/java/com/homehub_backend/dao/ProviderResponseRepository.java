package com.homehub_backend.dao;

import com.homehub_backend.entity.ProviderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProviderResponseRepository extends JpaRepository<ProviderResponse, UUID> {

//    boolean existsByRequestIdAndProviderId(UUID requestId, UUID providerId);
}
