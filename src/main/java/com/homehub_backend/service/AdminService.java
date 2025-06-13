package com.homehub_backend.service;


import com.homehub_backend.dao.AdminRepository;
import com.homehub_backend.dao.SocietyRepository;
import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.request.AdminProfileRequest;
import com.homehub_backend.dto.request.ResidentProfileRequest;
import com.homehub_backend.dto.response.AdminProfileResponse;
import com.homehub_backend.dto.response.ProfileResponse;
import com.homehub_backend.entity.Admin;
import com.homehub_backend.entity.Society;
import com.homehub_backend.entity.Users;
import jakarta.validation.Valid;
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
    @Autowired
    UserRepository userRepository;


    public ResponseEntity<ProfileResponse> createAdmin(UUID userId, @Valid AdminProfileRequest profileDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Society mySociety=societyRepository.findById(profileDto.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found with ID: " + profileDto.getSocietyId()));


        Admin newAdmin=Admin.builder()
                .user(user)
                .firstName(profileDto.getFirstName())
                .lastName(profileDto.getLastName())
                .society(mySociety)
                .createdAt(LocalDateTime.now())

                .build();

        Admin savedAdmin=adminRepository.save(newAdmin);
        return  ResponseEntity.ok(ProfileResponse.complete());

    }


    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminRepository.findAll());
    }

    public ResponseEntity<AdminProfileResponse> getAdminById(UUID id) {
        Admin admin=adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Society not found with ID: " + id));

        AdminProfileResponse res=AdminProfileResponse.builder()
                .name(admin.getFirstName()+" "+admin.getLastName())
                .societyId(admin.getSociety().getId())
                .build();

        return ResponseEntity.ok(res);
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
