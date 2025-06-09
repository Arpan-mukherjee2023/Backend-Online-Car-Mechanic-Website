package com.backend.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "garage_product")
@Data
public class GarageProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id")
    private ProductVariant productVariant;

    private int stock;



    // Getters and Setters
}
