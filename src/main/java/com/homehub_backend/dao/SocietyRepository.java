package com.homehub_backend.dao;

import com.homehub_backend.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SocietyRepository extends JpaRepository<Society, UUID> {
}
