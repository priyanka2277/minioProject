package com.example.minioproject.dto;

import com.example.minioproject.entity.Offer;
import com.example.minioproject.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message="Product name is required")
    private String name;
    @NotBlank(message="Product description is required")
    private String description;
    @NotNull(message="Product status is required")
    private ProductStatus status;
    private String imageUrl;
    @NotNull(message="Owner ID is required")
    private Long ownerId;
    private List<Long> offerIds;
}
