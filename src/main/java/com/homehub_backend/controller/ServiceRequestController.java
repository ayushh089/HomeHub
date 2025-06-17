package com.homehub_backend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.request.ServiceRequestDTO;
import com.homehub_backend.entity.ServiceRequest;
import com.homehub_backend.entity.Users;
import com.homehub_backend.service.ServiceRequestService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-requests")
public class ServiceRequestController {

    @Autowired
    ServiceRequestService serviceRequestService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ServiceRequest> createRequest(
            @RequestPart("request") @Valid String request,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            Authentication authentication) throws JsonProcessingException {
        System.out.println("Hey");
        String email = authentication.getName();
        Users resident = userRepository.findByEmail(email);
        System.out.println(resident);

        ServiceRequestDTO requestDTO=objectMapper.readValue(request,ServiceRequestDTO.class);

        System.out.println(files);
        return ResponseEntity.ok(serviceRequestService.createAndSubmitRequest(requestDTO,files, resident.getId()));
    }



}
