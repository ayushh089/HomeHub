package com.homehub_backend.controller;


import com.homehub_backend.dto.request.ServiceProviderSocietyRequest;
import com.homehub_backend.dto.response.ServiceProviderSocietyResponse;
import com.homehub_backend.service.ServiceProviderSocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/serviceProviderSociety")
public class ServiceProviderSocietyController {

    @Autowired
    ServiceProviderSocietyService serviceProviderSocietyService;

    @PostMapping
    public ResponseEntity<ServiceProviderSocietyResponse> addServiceProviderSociety(
            @RequestBody ServiceProviderSocietyRequest request
    ) {
        return serviceProviderSocietyService.addServiceProviderSociety(request);
    }

}
