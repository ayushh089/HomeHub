package com.homehub_backend.controller;

import com.homehub_backend.entity.Society;
import com.homehub_backend.service.SocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/society")
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class SocietyController {

    @Autowired
    private SocietyService societyService;

    @PostMapping
    public ResponseEntity<Society> registerSociety(@RequestBody Society st) {
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
}
