package com.homehub_backend.dao;

import com.homehub_backend.entity.ServiceProviderSociety;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceProviderSocietyRepository extends JpaRepository<ServiceProviderSociety, UUID> {
}
