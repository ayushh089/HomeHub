package com.homehub_backend.controller;

import com.homehub_backend.dto.ServiceProviderDto;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.request.ApprovalRequest;
import com.homehub_backend.dto.response.AdminProfileResponse;
import com.homehub_backend.entity.Admin;
import com.homehub_backend.service.AdminService;
import com.homehub_backend.service.ServiceProviderSocietyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    ServiceProviderSocietyService serviceProviderSocietyService;


    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminProfileResponse> getAdminById(@PathVariable UUID id) {
        System.out.println("hey");
        return adminService.getAdminById(id);
    }


    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<Admin>> getAdminsBySociety(@PathVariable UUID societyId) {
        return adminService.getAdminsBySociety(societyId);
    }

    @PutMapping("/service-providers/approval")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceProviderDto> approveOrRejectSP(
            @RequestParam UUID societyId,
            @RequestParam UUID serviceProviderId,
            @RequestBody ApprovalRequest approvalRequest,
            Authentication authentication) {

        try {
            // Get admin user from authentication
            String adminEmail = authentication.getName();
            System.out.println(adminEmail);

            return serviceProviderSocietyService
                    .updateApprovalStatus(societyId,serviceProviderId, approvalRequest, adminEmail);



        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


