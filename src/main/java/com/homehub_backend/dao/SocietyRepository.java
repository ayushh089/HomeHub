package com.homehub_backend.dao;

import com.homehub_backend.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SocietyRepository extends JpaRepository<Society, UUID> {
    List<Society> findByCityAndState(String city, String state);
    List<Society> findByApprovalStatus(Society.ApprovalStatus approvalStatus);
    List<Society> findByCityAndStateAndApprovalStatus(String city,String state,Society.ApprovalStatus approvalStatus);

    @Query("SELECT s FROM Society s WHERE s.city = :city AND s.state = :state " +
            "AND NOT EXISTS (SELECT 1 FROM ServiceProviderSociety sps " +
            "WHERE sps.society = s AND sps.serviceProvider.userId = :providerId)")
    List<Society> findAvailableSocieties(
            @Param("city") String city,
            @Param("state") String state,
            @Param("providerId") UUID providerId
    );
}
