package com.homehub_backend.service;


import com.homehub_backend.dao.UserRepository;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.entity.Resident;
import com.homehub_backend.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Users addUser(UserDto dto) {
        Users user = Users.builder()
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(dto.getPassword())
                .role(dto.getRole())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Users savedUser = userRepository.save(user);


        return savedUser;
    }

}
