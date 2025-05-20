package com.backend.demo.controller;

import com.backend.demo.DTO.ServiceHistoryDTO;
import com.backend.demo.entity.ServiceHistory;
import com.backend.demo.service.ServiceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/service-history")
public class ServiceHistoryController {

    private final ServiceHistoryService serviceHistoryService;

    @Autowired
    public ServiceHistoryController(ServiceHistoryService serviceHistoryService) {
        this.serviceHistoryService = serviceHistoryService;
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ServiceHistoryDTO>> getServiceHistoriesByUser(@PathVariable String userId) {
        return ResponseEntity.ok(serviceHistoryService.getServiceHistoriesByUserId(userId));
    }

}

