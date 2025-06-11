package com.backend.demo.controller;

import com.backend.demo.DTO.BillDTO;
import com.backend.demo.DTO.BillRequestDTO;
import com.backend.demo.DTO.OrderNotification;
import com.backend.demo.DTO.OrderRequestDTO;
import com.backend.demo.entity.Order;
import com.backend.demo.entity.Product;
import com.backend.demo.entity.ProductVariant;
import com.backend.demo.entity.User;
import com.backend.demo.repository.OrderRepository;
import com.backend.demo.service.OrderService;
import com.backend.demo.service.ProductService;
import com.backend.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

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

    @GetMapping("/garage/{garageId}")
    public List<OrderNotification> getOrdersByGarageId(@PathVariable String garageId) {
        List<Order> savedOrder = orderRepository.findByGarageId(garageId);
        List<OrderNotification> notifications = savedOrder.stream().map(order -> {
            OrderNotification notif = new OrderNotification();

            notif.setOrderDate(order.getOrderDate());
            notif.setPaymentStatus(order.getPaymentStatus());
            notif.setPaymentMode(order.getPaymentMode());

            Product product = productService.getProductById(order.getProductId());
            notif.setProductName(product.getProductName());

            Long targetVariantId = order.getVariantId();
            List<ProductVariant> variants = product.getVariants();
            String variantValue = variants.stream()
                    .filter(v -> v.getVariantId() == targetVariantId)
                    .map(ProductVariant::getVariantValue)
                    .findFirst()
                    .orElse(null);
            notif.setProductVariantName(variantValue);

            Optional<User> userOpt = userService.getUserById(order.getUserId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                notif.setUserName(user.getName());
                notif.setUserPhoneNumber(user.getPhone());
                notif.setUserEmailId(user.getEmail());
            } else {
                notif.setUserName("Unknown");
                notif.setUserPhoneNumber("N/A");
                notif.setUserEmailId("N/A");
            }

            notif.setTotalAmount(order.getTotalAmount());
            notif.setQuantity(order.getQuantity());

            return notif;
        }).collect(Collectors.toList());
        return notifications;
    }
}
