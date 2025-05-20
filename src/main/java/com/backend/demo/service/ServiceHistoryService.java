package com.backend.demo.service;

import com.backend.demo.DTO.ServiceHistoryDTO;
import com.backend.demo.entity.ServiceHistory;
import com.backend.demo.repository.ServiceHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceHistoryService {

    private final ServiceHistoryRepository serviceHistoryRepository;

    public ServiceHistoryService(ServiceHistoryRepository serviceHistoryRepository) {
        this.serviceHistoryRepository = serviceHistoryRepository;
    }

    public List<ServiceHistoryDTO> getServiceHistoriesByUserId(String userId) {
        return serviceHistoryRepository.fetchServiceHistoriesByUserId(userId);
    }


}

