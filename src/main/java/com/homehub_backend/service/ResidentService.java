package com.homehub_backend.service;

import com.homehub_backend.dao.ResidentRepository;
import com.homehub_backend.dao.SocietyRepository;
import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.request.ResidentProfileRequest;
import com.homehub_backend.dto.response.ProfileResponse;
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

    public ResponseEntity<Resident> getResidentById(UUID id) {
        return residentRepository.findById(id)
                .map(resident -> new ResponseEntity<>(resident, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Resident> updateResident(UUID id, UserDto dto) {
        Optional<Resident> optionalResident = residentRepository.findById(id);

        if (optionalResident.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resident resident = optionalResident.get();

        resident.setFirstName(dto.getFirstName());
        resident.setLastName(dto.getLastName());
        resident.setApartmentNumber(dto.getApartmentNumber());
        resident.setEmergencyContact(dto.getEmergencyContact());

        if (dto.getSocietyId() != null) {
            Society society = societyRepository.findById(dto.getSocietyId())
                    .orElseThrow(() -> new RuntimeException("Society not found"));
            resident.setSociety(society);
        }

        Resident updatedResident = residentRepository.save(resident);
        return new ResponseEntity<>(updatedResident, HttpStatus.OK);
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
