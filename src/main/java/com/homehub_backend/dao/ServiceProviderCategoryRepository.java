package com.homehub_backend.dao;

import com.homehub_backend.entity.ServiceProviderCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceProviderCategoryRepository  extends JpaRepository<ServiceProviderCategory, UUID> {
    @Query("SELECT spc FROM ServiceProviderCategory spc WHERE spc.serviceProvider.id = :serviceProviderId")
    List<ServiceProviderCategory> findByServiceProviderId(@Param("serviceProviderId") UUID serviceProviderId);

    @Modifying
    @Query("UPDATE ServiceProviderCategory sp SET sp.isPrimary = :isPrimary WHERE sp.serviceProvider.userId = :serviceProviderId")
    void updatePrimaryStatusForProvider(@Param("serviceProviderId") UUID serviceProviderId, @Param("isPrimary") Boolean isPrimary);

    List<ServiceProviderCategory> findByServiceProviderUserId(UUID userId);

    List<ServiceProviderCategory> findByServiceProviderUserIdAndIsPrimary(UUID userId, Boolean isPrimary);

    List<ServiceProviderCategory> findByCategoryId(UUID categoryId);


    @Query("SELECT spc FROM ServiceProviderCategory spc " +
            "WHERE spc.serviceProvider.id = :serviceProviderId " +
            "AND spc.category.id = :categoryId")
    List<ServiceProviderCategory> findByServiceProviderIdAndCategoryId(
            @Param("serviceProviderId") UUID serviceProviderId,
            @Param("categoryId") UUID categoryId);


}
