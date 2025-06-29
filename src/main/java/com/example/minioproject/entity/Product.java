package com.example.minioproject.entity;

import com.example.minioproject.enums.ProductStatus;
import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name="owner_id")
    private User owner;
    @ManyToMany(mappedBy = "products")
    private List<Offer> offers=new ArrayList<>();




}
