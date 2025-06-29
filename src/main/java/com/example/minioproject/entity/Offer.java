package com.example.minioproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToMany
    @JoinTable(name="offer_products",
    joinColumns = @JoinColumn(name="offer_id"),
            inverseJoinColumns = @JoinColumn(name="product_id")

    )
    private List<Product> products=new ArrayList<>();


}
