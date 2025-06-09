package com.backend.demo.controller;

import com.backend.demo.DTO.BillDTO;
import com.backend.demo.DTO.BillRequestDTO;
import com.backend.demo.DTO.OrderRequestDTO;
import com.backend.demo.entity.Order;
import com.backend.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/bill")
    public ResponseEntity<BillDTO> getBill(@RequestBody OrderRequestDTO dto) {
        BillDTO bill = orderService.generateBill(dto.getProductId(), dto.getVariantId(), dto.getQuantity(), dto.getGarageId());
        return ResponseEntity.ok(bill);
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDTO dto) {
        try {
            Order order = orderService.placeOrder(dto);
            return ResponseEntity.ok(order);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/calculate-bill")
    public ResponseEntity<BillDTO> calculateBill(@RequestBody BillRequestDTO request) {
        BillDTO bill = orderService.generateBill(request.getProductId(), request.getVariantId(), request.getQuantity(), request.getGarageId());
        return ResponseEntity.ok(bill);
    }
}
