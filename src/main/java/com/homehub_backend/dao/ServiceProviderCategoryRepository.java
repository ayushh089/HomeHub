package com.homehub_backend.dao;

import com.homehub_backend.entity.ServiceProviderCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceProviderCategoryRepository  extends JpaRepository<ServiceProviderCategory, UUID> {
}
