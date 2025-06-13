package com.homehub_backend.service;

import com.homehub_backend.dao.AdminRepository;
import com.homehub_backend.dao.SocietyRepository;
import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.request.SocietyRequestDto;
import com.homehub_backend.dto.response.SocietyDataResponse;
import com.homehub_backend.dto.response.SocietyFormResponse;
import com.homehub_backend.entity.Admin;
import com.homehub_backend.entity.Society;
import com.homehub_backend.entity.Users;
import com.homehub_backend.events.society.SocietyCreateRequestEvent;
import com.homehub_backend.events.society.SocietyRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SocietyService {

    @Autowired
    private SocietyRepository societyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    AdminRepository adminRepository;

    public ResponseEntity<SocietyFormResponse> addSociety(SocietyRequestDto dto) {

        Users requestedUser = userRepository.findById(dto.getRequestedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));


        Society society = Society.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .requestedBy(requestedUser)
                .approvalStatus(Society.ApprovalStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        societyRepository.save(society);
        System.out.println(society);
        SocietyFormResponse response = SocietyFormResponse.builder()
                .societyId(society.getId())
                .name(society.getName())
                .address(society.getAddress())
                .city(society.getCity())
                .pincode(society.getPincode())
                .requestedBy(society.getRequestedBy().getId())
                .email(society.getRequestedBy().getEmail())
                .number(society.getRequestedBy().getPhone())
                .build();


        eventPublisher.publishEvent(new SocietyCreateRequestEvent(
                this,
                response.getRequestedBy(),
                response.getSocietyId(),
                response.getName(),
                response.getEmail()
        ));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<List<Society>> getAllSociety() {
        List<Society> allSocieties = societyRepository.findAll();
        return ResponseEntity.ok(allSocieties);
    }

    public ResponseEntity<Society> getSocietyById(UUID id) {
        Optional<Society> society = societyRepository.findById(id);
        return society.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<Society> updateSociety(UUID id, Society updatedSociety) {
        return societyRepository.findById(id).map(existing -> {
            existing.setName(updatedSociety.getName());
            existing.setAddress(updatedSociety.getAddress());
            existing.setCity(updatedSociety.getCity());
            existing.setState(updatedSociety.getState());
            existing.setPincode(updatedSociety.getPincode());
            Society saved = societyRepository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<Void> deleteSociety(UUID id) {
        if (societyRepository.existsById(id)) {
            societyRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public List<SocietyDataResponse> getSocietiesByCityAndState(String city, String state, String status) {
        List<Society> societies = new ArrayList<>();
        if (status != null && status.equals("pending")) {
            societies = societyRepository.findByApprovalStatus(Society.ApprovalStatus.PENDING);
        } else {
            societies = societyRepository.findByCityAndState(city, state);

        }


        List<SocietyDataResponse> responseList = new ArrayList<>();
        for (Society society : societies) {


            SocietyDataResponse response = SocietyDataResponse.builder()
                    .id(society.getId())
                    .name(society.getName())
                    .address(society.getAddress())
                    .city(society.getCity())
                    .state(society.getState())
                    .pincode(society.getPincode())
                    .requestedByEmail(society.getRequestedBy().getEmail())
                    .requestedByPhone(society.getRequestedBy().getPhone())
                    .approvalStatus(society.getApprovalStatus().toString())
                    .createdAt(society.getCreatedAt())

                    .build();

            responseList.add(response);
        }

        return responseList;

    }
}
