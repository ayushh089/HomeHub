package com.homehub_backend.service;

import com.homehub_backend.dao.SocietyRepository;
import com.homehub_backend.entity.Society;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SocietyService {

    @Autowired
    private SocietyRepository societyRepository;

    public ResponseEntity<Society> addSociety(Society st) {
        Society society = societyRepository.save(st);
        return ResponseEntity.status(HttpStatus.CREATED).body(society);
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
}
