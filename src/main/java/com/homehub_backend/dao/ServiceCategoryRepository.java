package com.homehub_backend.dao;

import com.homehub_backend.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, UUID> {
}
