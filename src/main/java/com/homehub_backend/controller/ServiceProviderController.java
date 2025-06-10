package com.homehub_backend.controller;


import com.homehub_backend.dto.ServiceProviderDto;
import com.homehub_backend.dto.UserDto;
import com.homehub_backend.dto.response.ServiceProviderProfile;
import com.homehub_backend.entity.Resident;
import com.homehub_backend.entity.ServiceProvider;
import com.homehub_backend.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
//@RequestMapping("/serviceProvider")
public class ServiceProviderController {

    @Autowired
    ServiceProviderService serviceProviderService;

//    @PostMapping
//    public ResponseEntity<ServiceProviderDto> registerServiceProvider(@RequestBody ServiceProviderDto dto) {
//        return serviceProviderService.createServiceProvider(dto);
//    }
@GetMapping("/society/{societyId}/service-providers")
public ResponseEntity<List<ServiceProviderProfile>> getServiceProviderBySocietyId(
        @PathVariable UUID societyId,
        @RequestParam(required = false,value = "") UUID category) {
    System.out.println(societyId);

return serviceProviderService.getListBySocietyId(societyId,category);

        }

}
