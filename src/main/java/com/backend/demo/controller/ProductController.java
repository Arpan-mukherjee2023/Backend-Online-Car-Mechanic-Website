package com.backend.demo.controller;
import com.backend.demo.DTO.GarageProductDTO;
import com.backend.demo.DTO.ProductListDTO;
import com.backend.demo.entity.ProductVariant;
import com.backend.demo.service.GarageProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.backend.demo.entity.Product;
import com.backend.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @PostMapping("/garage")
    public ResponseEntity<?> addProductToGarage(@RequestBody GarageProductDTO dto) {

        productService.addGarageProduct(dto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product added successfully");
        return ResponseEntity.ok(response);
    }


}

