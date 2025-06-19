package com.homehub_backend.dao;

import com.homehub_backend.entity.RequestMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestMediaRepository extends JpaRepository<RequestMedia, UUID> {
    List<RequestMedia> findByServiceRequestId(UUID requestId);

}
