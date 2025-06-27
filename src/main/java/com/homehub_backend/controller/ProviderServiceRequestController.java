package com.homehub_backend.controller;

import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.request.ProviderResponseRequestDTO;
import com.homehub_backend.dto.response.ProviderResponseDTO;
import com.homehub_backend.dto.response.ServiceRequestResponseDTO;
import com.homehub_backend.entity.Users;
import com.homehub_backend.service.ProviderServiceRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/provider/service-requests")
@Tag(name = "Provider Service Requests", description = "APIs for service providers to manage service requests")
public class ProviderServiceRequestController {

    @Autowired
    private ProviderServiceRequestService providerServiceRequestService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/assigned")
    @Operation(summary = "Get assigned service requests",
            description = "Fetch service requests that are assigned to the provider")
    public ResponseEntity<Page<ServiceRequestResponseDTO>> getAssignedRequests(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir,
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            Authentication authentication) {

        String email = authentication.getName();
        Users provider = userRepository.findByEmail(email);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ServiceRequestResponseDTO> requests = providerServiceRequestService
                .getAssignedRequestsForProvider(provider.getId(), status, pageable);

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get service request details",
            description = "Get detailed information about a specific service request")
    public ResponseEntity<ServiceRequestResponseDTO> getRequestDetails(
            @Parameter(description = "Service request ID") @PathVariable UUID requestId,
            Authentication authentication) {

        String email = authentication.getName();
        Users provider = userRepository.findByEmail(email);

        System.out.println(provider);

        ServiceRequestResponseDTO request = providerServiceRequestService
                .getRequestDetailsForProvider(requestId, provider.getId());

        return ResponseEntity.ok(request);
    }


    @PostMapping("/{requestId}/respond")
    @Operation(summary = "Respond to a service request",
            description = "Allows provider to accept, reject, quote, or modify a service request")
    public ResponseEntity<ProviderResponseDTO> respondToRequest(
            @Parameter(description = "Service request ID") @PathVariable UUID requestId,
            @RequestBody ProviderResponseRequestDTO responseDTO,
            Authentication authentication) throws BadRequestException {

        String email = authentication.getName();
            Users provider = userRepository.findByEmail(email);

        ProviderResponseDTO response = providerServiceRequestService
                .respondToServiceRequest(requestId, provider.getId(), responseDTO);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{requestId}/responses")
//    @Operation(summary = "Get provider responses for a request",
//            description = "Retrieve all provider responses for a specific service request")
//    public ResponseEntity<ProviderResponseDTO> getProviderResponses(
//            @Parameter(description = "Service request ID") @PathVariable UUID requestId,
//            Authentication authentication) {
//
//        String email = authentication.getName();
//        Users provider = userRepository.findByEmail(email);
//
//        ProviderResponseDTO response = providerServiceRequestService
//                .getProviderResponseForRequest(requestId, provider.getId());
//
//        return ResponseEntity.ok(response);
//    }

}
