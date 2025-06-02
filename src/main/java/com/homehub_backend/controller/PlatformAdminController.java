package com.homehub_backend.controller;

import com.homehub_backend.dto.request.PlatformAdminRequest;
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

    @PostMapping
    public ResponseEntity<PlatformAdminResponse> registerAdmin(@RequestBody PlatformAdminRequest req) {
        return platformAdminService.createPlatformAdmin(req);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatformAdminResponse> getById(@PathVariable("id") UUID id) {
        return platformAdminService.getPlatformAdminById(id);
    }
    @PreAuthorize("hasRole('PLATFORM_ADMIN')")
    @GetMapping
    public ResponseEntity<List<PlatformAdminResponse>> getAll() {
        return platformAdminService.getAllPlatformAdmins();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatformAdminResponse> updatePlatformAdmin(@PathVariable("id") UUID id,
                                                                     @RequestBody PlatformAdminRequest req) {
        return platformAdminService.updatePlatformAdmin(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlatformAdmin(@PathVariable("id") UUID id) {
        return platformAdminService.deletePlatformAdmin(id);
    }
}
