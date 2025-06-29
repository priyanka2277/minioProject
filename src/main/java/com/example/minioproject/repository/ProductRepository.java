package com.example.minioproject.repository;

import com.example.minioproject.entity.Product;
import com.example.minioproject.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE p.status = :status")
    List<Product> findProductsByStatus(@Param("status") ProductStatus status);
    @Query(value = "SELECT * FROM product WHERE status = :status",
            nativeQuery = true)
    List<Product> findProductsByStatusNative(@Param("status") String status);
}
