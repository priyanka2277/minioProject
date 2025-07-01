package com.example.minioproject.Controller;

import com.example.minioproject.dto.OfferDto;
import com.example.minioproject.entity.Offer;
import com.example.minioproject.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
@Tag(name="Offer APIs")
public class OfferController {
    @Autowired
    private final OfferService offerService;
    @PostMapping
    @Operation(summary="Create the Offer to the products")
    public ResponseEntity<OfferDto> createOffer(@RequestBody @Valid OfferDto offerDto){
        return ResponseEntity.ok(offerService.createOffer(offerDto));
    }
    @PutMapping("/{id}")
    @Operation(summary="Update the Offer by id")
    public ResponseEntity<OfferDto> updateOffer(@PathVariable Long id,@RequestBody @Valid OfferDto offerDto){
        return ResponseEntity.ok(offerService.updateOffer(id,offerDto));
    }
    @GetMapping("/{id}")
    @Operation(summary="Get the Offer by Id")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable Long id){
        return ResponseEntity.ok(offerService.getOfferById(id));

    }
    @GetMapping
    @Operation(summary = "Get all Offer")
    public ResponseEntity<List<OfferDto>> getAllOffers(
            @RequestParam(value="pageNumber",defaultValue = "1",required = false) Integer pageNumber,
            @RequestParam(value="pageSize",defaultValue = "5",required = false) Integer pageSize

    ){
        return ResponseEntity.ok(offerService.getAllOffers(pageNumber,pageSize));
    }
    @PostMapping("/{offerId}/products/{productId}")
    @Operation(summary="Adding the products to offer")
    public ResponseEntity<OfferDto> addProductToOffer(@PathVariable Long offerId,@PathVariable Long productId){
        return ResponseEntity.ok(offerService.addProductToOffer(offerId,productId));
    }
    @DeleteMapping("/{offerId}/products/{productId}")
    @Operation(summary="Removing the products from offer")
    public ResponseEntity<OfferDto> removeProductFromOffer(@PathVariable Long offerId,@PathVariable Long productId){
        return ResponseEntity.ok(offerService.removeProductFromOffer(offerId,productId));
    }
}
