package com.backend.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //  Use auto-generated ID
    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "variant_type")
    private String variantType;

    @Column(name = "variant_value")
    private String variantValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @Column(name = "size")
    private String size;
}
