package com.backend.demo.service;

import com.backend.demo.DTO.BillDTO;
import com.backend.demo.DTO.OrderNotification;
import com.backend.demo.DTO.OrderRequestDTO;
import com.backend.demo.entity.*;
import com.backend.demo.repository.GarageProductRepository;
import com.backend.demo.repository.OrderRepository;
import com.backend.demo.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private GarageProductRepository garageProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;




    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public BillDTO generateBill(String productId, Long variantId, int quantity, String garageId) {
        GarageProduct gp = garageProductRepository.findByProductIdAndVariantId(productId, variantId)
                .stream()
                .filter(p -> p.getGarage().getGarageId().equals(garageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Garage product not found"));

        if (gp.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        double base = gp.getProduct().getProductPrice() * quantity;
        double gst = base * 0.18; // 18% GST
        double delivery = 50.0;
        double total = base + gst + delivery;

        return new BillDTO(base, gst, delivery, total);
    }

    
    public Order placeOrder(OrderRequestDTO request) {
        logger.info("Received order placement request: {}", request.toString());
        // 1. Find the garage product
        GarageProduct gp = garageProductRepository
                .findByProductIdAndVariantId(request.getProductId(), request.getVariantId())
                .stream()
                .filter(p -> p.getGarage().getGarageId().equals(request.getGarageId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Garage product not found"));

        // 2. Check stock availability
        if (gp.getStock() < request.getQuantity()) {
            throw new RuntimeException("Stock not sufficient");
        }

        // 3. Deduct stock and save
        int updatedStock = gp.getStock() - request.getQuantity();
        gp.setStock(updatedStock);
        garageProductRepository.save(gp);

        Product product = productService.getProductById(request.getProductId());
        Optional<User> user = userService.getUserById(request.getUserId());


        // 4. Calculate bill
        BillDTO bill = generateBill(request.getProductId(), request.getVariantId(), request.getQuantity(), request.getGarageId());

        // 5. Create order entity
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setGarageId(request.getGarageId());
        order.setProductId(request.getProductId());
        order.setVariantId(request.getVariantId());
        order.setQuantity(request.getQuantity());
        order.setPaymentMode(request.getPaymentMode());
        order.setPaymentStatus(
                request.getPaymentMode().equalsIgnoreCase("COD") ? "PENDING" : "INITIATED"
        );
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(bill.getTotalAmount());



        String subject = "Your order has been placed";

        String emailBody = String.format(
                "Dear %s,\n\n" +
                        "Thank you for your purchase!\n\n" +
                        "Your Order has been successfully placed.\n\n" +
                        "Here are the order details:\n" +
                        "Product: %s\n" +
                        "Quantity: %d\n" +
                        "Payment Mode: %s\n" +
                        "Total Amount: ₹%.2f\n\n" +
                        "We will notify the garage to process your order.\n\n" +
                        "Thank you for choosing Online Garage!\n\n" +
                        "Best Regards,\n" +
                        "Online Garage Team",
                order.getUserId(), product.getProductName(), request.getQuantity(), request.getPaymentMode(), order.getTotalAmount()
        );

        emailService.sendEmail(user.get().getEmail(), subject, emailBody);

        Order savedOrder = orderRepository.save(order);

        OrderNotification orderNotification = new OrderNotification();

        orderNotification.setPaymentMode(savedOrder.getPaymentMode());
        orderNotification.setUserName(user.get().getName());
        orderNotification.setUserEmailId(user.get().getEmail());
        orderNotification.setUserPhoneNumber(user.get().getPhone());

        Long targetVariantId = order.getVariantId();
        List<ProductVariant> variants = product.getVariants();
        String variantValue = variants.stream()
                .filter(v -> v.getVariantId() == targetVariantId)
                .map(ProductVariant::getVariantValue)
                .findFirst()
                .orElse(null);
        orderNotification.setProductVariantName(variantValue);
        orderNotification.setTotalAmount(order.getTotalAmount());
        orderNotification.setProductName(product.getProductName());
        orderNotification.setQuantity(order.getQuantity());
        orderNotification.setPaymentStatus(order.getPaymentStatus());
        orderNotification.setOrderDate(order.getOrderDate());



        // Web Socket configuration for sending message to the garage

        messagingTemplate.convertAndSend("/garage/" + order.getGarageId(), orderNotification);

        // 8. Save and return the order entity to the database
        return savedOrder;
    }

}
