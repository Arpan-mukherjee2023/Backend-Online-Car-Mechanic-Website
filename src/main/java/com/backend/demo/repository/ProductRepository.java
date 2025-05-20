package com.backend.demo.repository;

import com.backend.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    // custom methods if needed
}
