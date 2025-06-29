package com.example.minioproject.dto;

import com.example.minioproject.entity.Offer;
import com.example.minioproject.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private String name;
    private String description;
    private ProductStatus status;
    private String imageUrl;
    private Long ownerId;
    private List<Long> offerIds;
}
