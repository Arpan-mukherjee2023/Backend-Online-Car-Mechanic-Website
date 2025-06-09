package com.backend.demo.repository;

import com.backend.demo.DTO.ProductListDTO;
import com.backend.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    // custom methods if needed

}
