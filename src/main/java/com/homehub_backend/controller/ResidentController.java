package com.homehub_backend.controller;

import com.homehub_backend.dto.UserDto;
import com.homehub_backend.entity.Resident;
import com.homehub_backend.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resident")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    // Create a new resident
    @PostMapping
    public ResponseEntity<Resident> registerResident(@RequestBody UserDto dto) {
        return residentService.createResident(dto);
    }

    // Get all residents
    @GetMapping
    public ResponseEntity<List<Resident>> getAllResidents() {
        return residentService.getAllResidents();
    }

    // Get resident by ID
    @GetMapping("/{id}")
    public ResponseEntity<Resident> getResidentById(@PathVariable UUID id) {
        return residentService.getResidentById(id);
    }

    // Update resident
    @PutMapping("/{id}")
    public ResponseEntity<Resident> updateResident(@PathVariable UUID id, @RequestBody UserDto dto) {
        return residentService.updateResident(id, dto);
    }

    // Delete resident
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResident(@PathVariable UUID id) {
        return residentService.deleteResident(id);
    }
    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<Resident>> getResidentsBySociety(@PathVariable UUID societyId) {
        return residentService.getResidentsBySocietyId(societyId);
    }
}
