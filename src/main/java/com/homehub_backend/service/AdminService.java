package com.homehub_backend.service;


import com.homehub_backend.dao.AdminRepository;
import com.homehub_backend.dao.SocietyRepository;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.entity.Admin;
import com.homehub_backend.entity.Society;
import com.homehub_backend.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    SocietyRepository societyRepository;
    @Autowired
    UserService userService;


    public ResponseEntity<Admin> createAdmin(UserDto dto) {
        Users savedUser=userService.addUser(dto);
        Society mySociety = societyRepository.findById(dto.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found"));

        Admin newAdmin=Admin.builder()
                .user(savedUser)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .society(mySociety)
                .createdAt(LocalDateTime.now())

                .build();

        Admin savedAdmin=adminRepository.save(newAdmin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);

    }


    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminRepository.findAll());
    }

    public ResponseEntity<Admin> getAdminById(UUID id) {
        return adminRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Admin> updateAdmin(UUID id, UserDto dto) {
        return adminRepository.findById(id).map(existing -> {
            existing.setFirstName(dto.getFirstName());
            existing.setLastName(dto.getLastName());

            if (dto.getSocietyId() != null) {
                Society society = societyRepository.findById(dto.getSocietyId())
                        .orElseThrow(() -> new RuntimeException("Society not found"));
                existing.setSociety(society);
            }

            return new ResponseEntity<>(adminRepository.save(existing), HttpStatus.OK);
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deleteAdmin(UUID id) {
        return adminRepository.findById(id).map(admin -> {
            adminRepository.delete(admin);
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Admin>> getAdminsBySociety(UUID societyId) {
        List<Admin> admins = adminRepository.findBySocietyId(societyId);
        return ResponseEntity.ok(admins);
    }
}
