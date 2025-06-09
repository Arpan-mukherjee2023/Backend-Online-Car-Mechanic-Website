package com.backend.demo.service;

import com.backend.demo.DTO.GarageInfoDTO;
import com.backend.demo.DTO.GarageProductDTO;
import com.backend.demo.DTO.ProductListDTO;
import com.backend.demo.entity.GarageProduct;
import com.backend.demo.entity.Product;
import com.backend.demo.entity.ProductVariant;
import com.backend.demo.repository.GarageProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GarageProductService {

    @Autowired
    private GarageProductRepository garageProductRepository;

    public List<ProductListDTO> getProductsByGarageId(String garageId) {
        List<GarageProduct> garageProducts = garageProductRepository.findByGarage_GarageId(garageId);

        return garageProducts.stream().map(gp -> {
            ProductListDTO dto = new ProductListDTO();

            // garageId from Garage entity
            dto.setGarageId(gp.getGarage().getGarageId());

            // product details from Product entity
            dto.setProductId(gp.getProduct().getProductId());
            dto.setProductName(gp.getProduct().getProductName());
            dto.setProductDesc(gp.getProduct().getProductDesc());
            dto.setProductPrice(gp.getProduct().getProductPrice());
            dto.setProductUnit(gp.getProduct().getProductUnit());

            // variant info from productVariant, check for null
            ProductVariant variant = gp.getProductVariant();
            if (variant != null) {
                dto.setVariantType(variant.getVariantType());
                dto.setVariantValue(variant.getVariantValue());
            } else {
                dto.setVariantType(null);
                dto.setVariantValue(null);
            }

            // stock from GarageProduct entity
            dto.setStock(gp.getStock());

            return dto;
        }).collect(Collectors.toList());
    }

    public List<GarageInfoDTO> findGaragesByProductAndVariant(String productId, Long variantId) {
        List<GarageProduct> garageProducts = garageProductRepository
                .findByProductIdAndVariantId(productId, variantId);

        return garageProducts.stream()
                .map(gp -> new GarageInfoDTO(
                        gp.getGarage().getGarageId(),
                        gp.getGarage().getName(),
                        gp.getStock()
                ))
                .collect(Collectors.toList());
    }
}
