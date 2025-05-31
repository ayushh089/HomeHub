package com.homehub_backend.dao;

import com.homehub_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    List<Admin> findBySocietyId(UUID societyId);
}
