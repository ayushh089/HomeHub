package com.homehub_backend.dao;

import com.homehub_backend.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<Resident, UUID> {
    List<Resident> findBySocietyId(UUID societyId);
}
