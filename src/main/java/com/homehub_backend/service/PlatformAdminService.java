package com.homehub_backend.service;

import com.homehub_backend.dao.AdminRepository;
import com.homehub_backend.dao.PlatformAdminRepository;
import com.homehub_backend.dao.SocietyRepository;
import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.request.PlatformAdminProfileRequest;
import com.homehub_backend.dto.response.AdminProfileResponse;
import com.homehub_backend.dto.response.PlatformAdminResponse;
import com.homehub_backend.dto.response.ProfileResponse;
import com.homehub_backend.entity.Admin;
import com.homehub_backend.entity.PlatformAdmin;
import com.homehub_backend.entity.Society;
import com.homehub_backend.entity.Users;
import jdk.jfr.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlatformAdminService {

    @Autowired
    private PlatformAdminRepository platformAdminRepository;

    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    SocietyService societyService;

    @Autowired
    SocietyRepository societyRepository;

    @Lazy
    @Autowired
    AuthService authService;

    @Autowired
    AdminRepository adminRepository;

    public ResponseEntity<ProfileResponse> createPlatformAdmin(UUID userId, PlatformAdminProfileRequest req) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        PlatformAdmin newPfAdmin = PlatformAdmin.builder()
                .user(user)
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build();

        PlatformAdmin savedPfAdmin = platformAdminRepository.save(newPfAdmin);
        return ResponseEntity.ok(ProfileResponse.complete());
    }

    public ResponseEntity<PlatformAdminResponse> getPlatformAdminById(UUID id) {
        Optional<PlatformAdmin> optAdmin = platformAdminRepository.findById(id);
        if (optAdmin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PlatformAdmin admin = optAdmin.get();
        PlatformAdminResponse response = mapToResponse(admin.getUser(), admin);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<PlatformAdminResponse>> getAllPlatformAdmins() {
        List<PlatformAdmin> admins = platformAdminRepository.findAll();
        List<PlatformAdminResponse> responses = admins.stream()
                .map(admin -> mapToResponse(admin.getUser(), admin))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

//    public ResponseEntity<PlatformAdminResponse> updatePlatformAdmin(UUID id, PlatformAdminProfileRequest req) {
//        Optional<PlatformAdmin> optAdmin = platformAdminRepository.findById(id);
//        if (optAdmin.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        PlatformAdmin existingAdmin = optAdmin.get();
//
//        // Update User fields via UserService (assuming it has updateUser)
//        Users existingUser = existingAdmin.getUser();
//        existingUser.setEmail(req.getEmail());
//        existingUser.setPhone(req.getPhone());
//        existingUser.setRole(req.getRole());
//        // You can add password update logic if needed
//
//        userService.updateUser(existingUser); // implement this method in UserService
//
//        // Update PlatformAdmin fields
//        existingAdmin.setFirstName(req.getFirstName());
//        existingAdmin.setLastName(req.getLastName());
//
//        PlatformAdmin updatedAdmin = platformAdminRepository.save(existingAdmin);
//        PlatformAdminResponse response = mapToResponse(updatedAdmin.getUser(), updatedAdmin);
//
//        return ResponseEntity.ok(response);
//    }

    public ResponseEntity<Void> deletePlatformAdmin(UUID id) {
        Optional<PlatformAdmin> optAdmin = platformAdminRepository.findById(id);
        if (optAdmin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        platformAdminRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private PlatformAdminResponse mapToResponse(Users user, PlatformAdmin admin) {
        return PlatformAdminResponse.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())

                .build();
    }

    public String approveSociety(UUID id) {
        Society society = societyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Society not found with id: " + id));

        if (society.getApprovalStatus() == Society.ApprovalStatus.APPROVED) {
            return "Society is already approved.";
        }

        // Get the currently authenticated platform admin
        Users user = userRepository.findByEmail(
                authService.getCurrentUserId()); // You should implement this in AuthService
        if (!Objects.equals(user.getRole(), "PLATFORM_ADMIN")) {
            return "No valid User Found";
        }

        PlatformAdmin admin = platformAdminRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("PlatformAdmin Not found" + user.getId()));

        society.setApprovalStatus(Society.ApprovalStatus.APPROVED);
        society.setApprovedAt(LocalDateTime.now());
        society.setApprovedBy(admin);

        societyRepository.save(society);

        return "Society approved successfully";
    }

    public ResponseEntity<List<AdminProfileResponse>> getAdminsBySociety(UUID id) {
            List<Admin> adminList=adminRepository.findBySocietyId(id);

            List<AdminProfileResponse> responseList=new ArrayList<>();

            for(Admin it:adminList){
//                Users user=userRepository.findById(it.getUserId()).orElseThrow(() -> new RuntimeException("Society not found with id: " + it.getUserId()));
                AdminProfileResponse res=AdminProfileResponse.builder()
                        .name(it.getFirstName()+" "+ it.getLastName())
                        .email(it.getUser().getEmail())
                        .phone(it.getUser().getPhone())
                        .build();

                responseList.add(res);
            }
            return ResponseEntity.ok(responseList);
    }
}
