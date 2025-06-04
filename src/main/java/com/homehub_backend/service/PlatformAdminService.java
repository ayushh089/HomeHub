package com.homehub_backend.service;

import com.homehub_backend.dao.PlatformAdminRepository;
import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.request.PlatformAdminProfileRequest;
import com.homehub_backend.dto.response.PlatformAdminResponse;
import com.homehub_backend.dto.response.ProfileResponse;
import com.homehub_backend.entity.PlatformAdmin;
import com.homehub_backend.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlatformAdminService {

    @Autowired
    private PlatformAdminRepository platformAdminRepository;

    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<ProfileResponse> createPlatformAdmin(UUID userId,PlatformAdminProfileRequest req) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        PlatformAdmin newPfAdmin = PlatformAdmin.builder()
                .user(user)
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .build();

        PlatformAdmin savedPfAdmin = platformAdminRepository.save(newPfAdmin);
        return  ResponseEntity.ok(ProfileResponse.complete());
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
}
