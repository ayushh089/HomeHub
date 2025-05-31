package com.homehub_backend.dao;

import com.homehub_backend.entity.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, UUID> {
}
