package com.backend.demo.controller;

import com.backend.demo.DTO.GarageInfoDTO;
import com.backend.demo.DTO.ProductListDTO;
import com.backend.demo.service.GarageProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garageProducts")
@CrossOrigin(origins = "http://localhost:5173")

public class GarageProductController {

    @Autowired
    private GarageProductService garageProductService;


    // lists all the products that a garage sells
    @GetMapping("/{garageId}")
    public ResponseEntity<List<ProductListDTO>> getGarageProducts(@PathVariable String garageId) {
        List<ProductListDTO> products = garageProductService.getProductsByGarageId(garageId);
        return ResponseEntity.ok(products);
    }

    // returns the garage_id and garage_name and stock from the garages which sells the product with given variant
    @GetMapping("/product/{productId}/variant/{variantId}")
    public ResponseEntity<List<GarageInfoDTO>> getGaragesByProductAndVariant(
            @PathVariable String productId,
            @PathVariable Long variantId) {
        List<GarageInfoDTO> garages = garageProductService.findGaragesByProductAndVariant(productId, variantId);
        return ResponseEntity.ok(garages);
    }
}

