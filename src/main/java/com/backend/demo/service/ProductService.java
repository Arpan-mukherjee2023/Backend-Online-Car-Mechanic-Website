package com.backend.demo.service;
import com.backend.demo.DTO.GarageProductDTO;
import com.backend.demo.DTO.ProductListDTO;
import com.backend.demo.entity.Garage;
import com.backend.demo.entity.GarageProduct;
import com.backend.demo.entity.ProductVariant;
import com.backend.demo.repository.GarageProductRepository;
import com.backend.demo.repository.GarageRepository;
import com.backend.demo.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.backend.demo.entity.Product;
import com.backend.demo.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private GarageProductRepository garageProductRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null); // Or throw an exception if not found
    }

    public List<ProductVariant> getVariantsByProductId(String productId) {
        return productVariantRepository.findByProductProductId(productId);
    }

    public void addGarageProduct(GarageProductDTO dto) {
        Garage garage = garageRepository.findById(dto.getGarageId())
                .orElseThrow(() -> new RuntimeException("Garage not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Fetch product variant by variantId (Long)
        ProductVariant productVariant = productVariantRepository.findById(dto.getVariantId())
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        GarageProduct gp = new GarageProduct();
        gp.setGarage(garage);
        gp.setProduct(product);
        gp.setProductVariant(productVariant);
        gp.setStock(dto.getStock());

        garageProductRepository.save(gp);
    }




}
