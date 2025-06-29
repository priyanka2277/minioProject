package com.example.minioproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferDto {
    @NotBlank(message="Title is required")
    private String title;
    @NotBlank(message="Description is required")
    private String description;
    @NotEmpty(message ="Product Ids list must not be empty")
    private List<Long> productIds;

}
