package com.example.minioproject.Controller;

import com.example.minioproject.dto.ProductDto;
import com.example.minioproject.entity.Product;
import com.example.minioproject.enums.ProductStatus;
import com.example.minioproject.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @Autowired
    private ObjectMapper objectMapper;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createProduct(
            @RequestPart("product") String productData,
            @RequestPart("image")  MultipartFile imageFile) {

        try {
            ProductDto productDto =
                    objectMapper.readValue(productData, ProductDto.class);

            productService.createProduct(productDto, imageFile);
            return ResponseEntity.ok("Product created successfully");

        } catch (JsonProcessingException e) {
            // bad JSON from client
            return ResponseEntity.badRequest()
                    .body("Invalid product JSON: " + e.getOriginalMessage());
        } catch (Exception e) {
            // upload error, DB error, etc.
            return ResponseEntity.internalServerError()
                    .body("Server error: " + e.getMessage());
        }
    }
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") String productData,
            @RequestPart("image") MultipartFile imageFile
    ) throws Exception {

        ProductDto productDto = objectMapper.readValue(productData, ProductDto.class);
        productService.updateProduct(id, productDto, imageFile);
        return ResponseEntity.ok("Product updated successfully");
    }




    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @PatchMapping("/{id}/rent")
    public ResponseEntity<String> rentProduct(@PathVariable Long id){
        productService.changeStatus(id, ProductStatus.RENTED);
        return ResponseEntity.ok("Product rented");
    }
    @PatchMapping("/{id}/donate")
    public ResponseEntity<String> donateProduct(@PathVariable Long id){
        productService.changeStatus(id,ProductStatus.DONATED);
        return ResponseEntity.ok("Product donated");
    }
    @GetMapping("/rented")
    public ResponseEntity<List<ProductDto>> getRentedProducts(){
        return ResponseEntity.ok(productService.getProductByStatusJPQL(ProductStatus.RENTED));
    }
    @GetMapping("/donated")
    public ResponseEntity<List<ProductDto>> getDonatedProducts(){
        return ResponseEntity.ok(productService.getProductByStatusNative(ProductStatus.DONATED));



    }

}
