package com.homehub_backend.controller;


import com.homehub_backend.dto.request.ServiceProviderSocietyRequest;
import com.homehub_backend.dto.response.ServiceProviderProfile;
import com.homehub_backend.dto.response.ServiceProviderSocietyResponse;
import com.homehub_backend.service.ServiceProviderSocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/serviceProviderSociety")
public class ServiceProviderSocietyController {

    @Autowired
    ServiceProviderSocietyService serviceProviderSocietyService;

    @PostMapping
//    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ServiceProviderSocietyResponse> addServiceProviderSociety(
            @RequestBody ServiceProviderSocietyRequest request
    ) {
        System.out.println(request);
        return serviceProviderSocietyService.addServiceProviderSociety(request);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ServiceProviderProfile>> getPendingServiceProvidersForSociety(
            @RequestParam UUID societyId) {

        return  serviceProviderSocietyService.getPendingApprovalsForSociety(societyId);

    }

}
