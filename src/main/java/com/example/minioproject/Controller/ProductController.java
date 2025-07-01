package com.example.minioproject.Controller;

import com.example.minioproject.dto.ProductDto;
import com.example.minioproject.entity.Product;
import com.example.minioproject.enums.ProductStatus;
import com.example.minioproject.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@Tag(name="Product APIs")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Validator validator;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary="Create the product")
    public ResponseEntity<String> createProduct(
            @RequestPart("product") String productData,
            @RequestPart("image")  MultipartFile imageFile) {

        try {
            ProductDto productDto =
                    objectMapper.readValue(productData, ProductDto.class);
            //Manual validation
            Set<ConstraintViolation<ProductDto>> violations=validator.validate(productDto);
            if(!violations.isEmpty()){
                StringBuilder errors=new StringBuilder();
                for(ConstraintViolation<ProductDto> violation:violations){
                    errors.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
                }
                return ResponseEntity.badRequest().body("Validation errors: " + errors.toString());
            }

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
    @Operation(summary = "Update the product by id")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") String productData,
            @RequestPart("image") MultipartFile imageFile
    ) throws Exception {

        ProductDto productDto = objectMapper.readValue(productData, ProductDto.class);
        Set<ConstraintViolation<ProductDto>> violations=validator.validate(productDto);
        if(!violations.isEmpty()){
            StringBuilder errors=new StringBuilder();
            for(ConstraintViolation<ProductDto> violation:violations){
                errors.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body("Validation errors: " + errors.toString());
        }
        productService.updateProduct(id, productDto, imageFile);
        return ResponseEntity.ok("Product updated successfully");
    }




    @GetMapping("/{id}")
    @Operation(summary="Get Product By Id")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }
    @GetMapping
    @Operation(summary="Get all Products")
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize) {

        return ResponseEntity.ok(productService.getAllProducts(pageNumber, pageSize));
    }

    @PatchMapping("/{id}/rent")
    @Operation(summary="Make the product rented by id")
    public ResponseEntity<String> rentProduct(@PathVariable Long id){
        productService.changeStatus(id, ProductStatus.RENTED);
        return ResponseEntity.ok("Product rented");
    }
    @PatchMapping("/{id}/donate")
    @Operation(summary="Make the product donated by id")
    public ResponseEntity<String> donateProduct(@PathVariable Long id){
        productService.changeStatus(id,ProductStatus.DONATED);
        return ResponseEntity.ok("Product donated");
    }
    @GetMapping("/rented")
    @Operation(summary="Get all rented products")
    public ResponseEntity<List<ProductDto>> getRentedProducts(){
        return ResponseEntity.ok(productService.getProductByStatusJPQL(ProductStatus.RENTED));
    }
    @GetMapping("/donated")
    @Operation(summary="Get all donated products")
    public ResponseEntity<List<ProductDto>> getDonatedProducts(){
        return ResponseEntity.ok(productService.getProductByStatusNative(ProductStatus.DONATED));



    }

}
