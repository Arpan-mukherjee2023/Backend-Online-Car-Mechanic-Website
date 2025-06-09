package com.backend.demo.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/razorpay")
public class RazorPayController {

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> requestData) {
        try {
            RazorpayClient razorpay = new RazorpayClient("YOUR KEY ID", "YOUR SECRET KEY");

            int amount = ((Number) requestData.get("amount")).intValue() * 100; // Razorpay uses paise
            String currency = (String) requestData.get("currency");

            JSONObject options = new JSONObject();
            options.put("amount", amount);
            options.put("currency", currency);
            options.put("payment_capture", 1);

            Order order = razorpay.orders.create(options);

            Map<String, Object> response = new HashMap<>();
            response.put("id", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Razorpay order creation failed"));
        }
    }
}
