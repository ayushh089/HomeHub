package com.homehub_backend.controller;

import com.homehub_backend.dto.UserDto;
import com.homehub_backend.entity.Admin;
import com.homehub_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;


    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable UUID id) {
        return adminService.getAdminById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable UUID id, @RequestBody UserDto dto) {
        return adminService.updateAdmin(id, dto);
    }

    @GetMapping("/society/{societyId}")
    public ResponseEntity<List<Admin>> getAdminsBySociety(@PathVariable UUID societyId) {
        return adminService.getAdminsBySociety(societyId);
    }

}


