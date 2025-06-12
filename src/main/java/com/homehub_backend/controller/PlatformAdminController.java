package com.homehub_backend.controller;

import com.homehub_backend.dto.request.PlatformAdminProfileRequest;
import com.homehub_backend.dto.response.AdminProfileResponse;
import com.homehub_backend.dto.response.PlatformAdminResponse;
import com.homehub_backend.service.PlatformAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController  // @RestController = @Controller + @ResponseBody
@RequestMapping("/platformAdmin")
public class PlatformAdminController {

    @Autowired
    private PlatformAdminService platformAdminService;



    @GetMapping("/{id}")
    public ResponseEntity<PlatformAdminResponse> getById(@PathVariable("id") UUID id) {
        return platformAdminService.getPlatformAdminById(id);
    }
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @GetMapping
    public ResponseEntity<List<PlatformAdminResponse>> getAll() {
        return platformAdminService.getAllPlatformAdmins();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatformAdmin(@PathVariable("id") UUID id) {
        return platformAdminService.deletePlatformAdmin(id);
    }

    @PutMapping("/approveSociety/{id}")
    public String approveSociety(@PathVariable("id") UUID id){
        return platformAdminService.approveSociety(id);
    }

    @GetMapping("/getAdmins/{id}")
    public ResponseEntity<List<AdminProfileResponse>> getAdminBySocietyId(@PathVariable("id") UUID id) {
        return platformAdminService.getAdminsBySociety(id);
    }
}
