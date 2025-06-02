package com.homehub_backend.dao;

import com.homehub_backend.entity.PlatformAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlatformAdminRepository extends JpaRepository<PlatformAdmin, UUID> {
}
