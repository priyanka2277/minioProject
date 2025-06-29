package com.example.minioproject.Controller;

import com.example.minioproject.dto.OfferDto;
import com.example.minioproject.entity.Offer;
import com.example.minioproject.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {
    @Autowired
    private final OfferService offerService;
    @PostMapping
    public ResponseEntity<OfferDto> createOffer(@RequestBody @Valid OfferDto offerDto){
        return ResponseEntity.ok(offerService.createOffer(offerDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<OfferDto> updateOffer(@PathVariable Long id,@RequestBody @Valid OfferDto offerDto){
        return ResponseEntity.ok(offerService.updateOffer(id,offerDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable Long id){
        return ResponseEntity.ok(offerService.getOfferById(id));

    }
    @GetMapping
    public ResponseEntity<List<OfferDto>> getAllOffers(){
        return ResponseEntity.ok(offerService.getAllOffers());
    }
    @PostMapping("/{offerId}/products/{productId}")
    public ResponseEntity<OfferDto> addProductToOffer(@PathVariable Long offerId,@PathVariable Long productId){
        return ResponseEntity.ok(offerService.addProductToOffer(offerId,productId));
    }
    @DeleteMapping("/{offerId}/products/{productId}")
    public ResponseEntity<OfferDto> removeProductFromOffer(@PathVariable Long offerId,@PathVariable Long productId){
        return ResponseEntity.ok(offerService.removeProductFromOffer(offerId,productId));
    }
}
