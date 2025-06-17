package com.backend.demo.repository;

import com.backend.demo.entity.GarageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarageProductRepository extends JpaRepository<GarageProduct, Long> {
    List<GarageProduct> findByGarage_GarageId(String garageId);

    @Query("SELECT gp FROM GarageProduct gp WHERE gp.product.productId = :productId AND gp.productVariant.variantId = :variantId")
    List<GarageProduct> findByProductIdAndVariantId(@Param("productId") String productId, @Param("variantId") Long variantId);
    Optional<GarageProduct> findByGarage_GarageIdAndProduct_ProductIdAndProductVariant_VariantId(String garageId, String productId, Long variantId);
}
