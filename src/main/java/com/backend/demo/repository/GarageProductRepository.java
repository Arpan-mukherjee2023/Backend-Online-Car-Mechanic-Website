package com.backend.demo.repository;

import com.backend.demo.DTO.ProductListDTO;
import com.backend.demo.entity.GarageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageProductRepository extends JpaRepository<GarageProduct, Long> {
    List<GarageProduct> findByGarage_GarageId(String garageId);

    @Query("SELECT gp FROM GarageProduct gp WHERE gp.product.productId = :productId AND gp.productVariant.variantId = :variantId")
    List<GarageProduct> findByProductIdAndVariantId(@Param("productId") String productId, @Param("variantId") Long variantId);
}
