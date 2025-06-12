package com.homehub_backend.controller;

import com.homehub_backend.dto.request.SocietyRequestDto;
import com.homehub_backend.dto.response.ServiceProviderApprovalReqResponse;
import com.homehub_backend.dto.response.SocietyFormResponse;
import com.homehub_backend.entity.Society;
import com.homehub_backend.service.ServiceProviderSocietyService;
import com.homehub_backend.service.SocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/society")
//@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class SocietyController {

    @Autowired
    private SocietyService societyService;

    @Autowired
    ServiceProviderSocietyService serviceProviderSocietyService;

    @PostMapping
    public ResponseEntity<SocietyFormResponse> registerSociety(@RequestBody SocietyRequestDto st) {
        System.out.println(st);
        return societyService.addSociety(st);
    }

    @GetMapping
    public ResponseEntity<List<Society>> getAllSocieties() {
        return societyService.getAllSociety();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Society> getSocietyById(@PathVariable UUID id) {
        return societyService.getSocietyById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Society> updateSociety(@PathVariable UUID id, @RequestBody Society updatedSociety) {
        return societyService.updateSociety(id, updatedSociety);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSociety(@PathVariable UUID id) {
        return societyService.deleteSociety(id);
    }

    @GetMapping("/{id}/service-provider-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ServiceProviderApprovalReqResponse>> getServiceProviderRequestBySocietyId(
            @PathVariable UUID id) {

        List<ServiceProviderApprovalReqResponse> requests =
                serviceProviderSocietyService.getServiceProviderRequestsBySocietyId(id);

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/getSocieties")
    public ResponseEntity<List<Society>> getListByCityAndState(@RequestParam String city,
                                                               @RequestParam String state){
        List<Society> societies=societyService.getSocietiesByCityAndState(city, state);
        return ResponseEntity.ok(societies);
    }
}
