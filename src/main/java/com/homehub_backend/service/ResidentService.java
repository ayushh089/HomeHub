package com.homehub_backend.service;

import com.homehub_backend.dao.ResidentRepository;
import com.homehub_backend.dao.SocietyRepository;
import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.request.ResidentProfileRequest;
import com.homehub_backend.dto.response.ProfileResponse;
import com.homehub_backend.dto.response.ResidentProfileResponse;
import com.homehub_backend.entity.Resident;
import com.homehub_backend.entity.Society;
import com.homehub_backend.entity.Users;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ResidentService {

    @Autowired
    ResidentRepository residentRepository;
    @Autowired
    SocietyRepository societyRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;


    public ResponseEntity<ProfileResponse> createResident(UUID userId, @Valid ResidentProfileRequest profileDto) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Society mySociety=societyRepository.findById(profileDto.getSocietyId())
                .orElseThrow(() -> new RuntimeException("Society not found with ID: " + profileDto.getSocietyId()));

        Resident newResident = Resident.builder()
                .user(user)
                .firstName(profileDto.getFirstName())
                .lastName(profileDto.getLastName())
                .apartmentNumber(profileDto.getApartmentNumber())
                .society(mySociety)
                .emergencyContact(profileDto.getEmergencyContact())
                .createdAt(LocalDateTime.now())

                .build();

        Resident savedResident = residentRepository.save(newResident);
        return  ResponseEntity.ok(ProfileResponse.complete());

    }

    public ResponseEntity<List<Resident>> getAllResidents() {
        return new ResponseEntity<>(residentRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<ResidentProfileResponse> getResidentById(UUID id) {
      Resident resident=residentRepository.findById(id)
              .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

      ResidentProfileResponse response=ResidentProfileResponse.builder()
              .societyName(resident.getSociety().getName())
              .societyId(resident.getSociety().getId())
              .societyAddress(resident.getSociety().getAddress())
              .apartmentNumber(resident.getApartmentNumber())
              .firstName(resident.getFirstName())
              .lastName(resident.getLastName())
              .email(resident.getUser().getEmail())
              .build();
      return ResponseEntity.ok(response);
    }



    public ResponseEntity<Void> deleteResident(UUID id) {
        if (!residentRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        residentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<List<Resident>> getResidentsBySocietyId(UUID societyId) {
        List<Resident> residents = residentRepository.findBySocietyId(societyId);
        return new ResponseEntity<>(residents, HttpStatus.OK);
    }

}
