package com.homehub_backend.controller;


import com.homehub_backend.dto.request.ServiceCategoryRequest;
import com.homehub_backend.dto.request.ServiceProviderCategoryRequest;
import com.homehub_backend.dto.response.ServiceCategoryResponse;
import com.homehub_backend.dto.response.ServiceProviderCategoryResponse;
import com.homehub_backend.service.ServiceProviderCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-provider-categories")
public class ServiceProviderCategoryController {

    @Autowired
    ServiceProviderCategoryService serviceProviderCategoryService;

    @PostMapping
    @PreAuthorize("hasRole('SERVICE_PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<ServiceProviderCategoryResponse> addServiceProviderCategory(@RequestBody ServiceProviderCategoryRequest dto){
        return serviceProviderCategoryService.addServiceProviderCategory(dto);
    }

    @GetMapping("/provider/{serviceProviderId}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<List<ServiceProviderCategoryResponse>> getServiceProviderCategories(
            @PathVariable UUID serviceProviderId) {
        System.out.println("Hey");
        List<ServiceProviderCategoryResponse> categories = serviceProviderCategoryService
                .getServiceProviderCategories(serviceProviderId);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER') or hasRole('ADMIN')")
    public ResponseEntity<ServiceProviderCategoryResponse> updateServiceProviderCategory(
            @PathVariable UUID id,
            @Valid @RequestBody ServiceProviderCategoryRequest request) {
        System.out.println("hey");
        ServiceProviderCategoryResponse response = serviceProviderCategoryService
                .updateServiceProviderCategory(id, request);
        return ResponseEntity.ok(response);
    }
//
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('SERVICE_PROVIDER') or hasRole('ADMIN')")
//    public ResponseEntity<Void> deleteServiceProviderCategory(@PathVariable Long id) {
//        serviceProviderCategoryService.deleteServiceProviderCategory(id);
//        return ResponseEntity.noContent().build();
//    }
}
