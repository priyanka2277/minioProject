package com.example.minioproject.service;

import com.example.minioproject.dto.OfferDto;
import com.example.minioproject.entity.Offer;
import com.example.minioproject.entity.Product;
import com.example.minioproject.exception.OfferNotFoundException;
import com.example.minioproject.repository.OfferRepository;
import com.example.minioproject.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OfferService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    public OfferDto createOffer(OfferDto offerDto){
        Offer offer=new Offer();
        offer.setTitle(offerDto.getTitle());
        offer.setDescription(offerDto.getDescription());
        List<Product> products=new ArrayList<>();
        if(offerDto.getProductIds()!=null){
             products=productRepository.findAllById(offerDto.getProductIds());
            offer.setProducts(products);
        }
        Offer savedOffer=offerRepository.save(offer);
        OfferDto dto=new OfferDto();
        dto.setTitle(savedOffer.getTitle());
        dto.setDescription(savedOffer.getDescription());
        dto.setProductIds(products.stream().map(Product::getId).toList());
        return dto;
    }
    public OfferDto updateOffer(Long id,OfferDto offerDto){
        Offer offer=offerRepository.findById(id).orElseThrow(()->new RuntimeException("offer not found"));
        offer.setTitle(offerDto.getTitle());
        offer.setDescription(offerDto.getDescription());
        List<Product> products=new ArrayList<>();
        if(offerDto.getProductIds()!=null){
             products=productRepository.findAllById(offerDto.getProductIds());
            offer.setProducts(products);
        }
        Offer savedOffer=offerRepository.save(offer);
        OfferDto dto=new OfferDto();
        dto.setTitle(savedOffer.getTitle());
        dto.setDescription(savedOffer.getDescription());
        dto.setProductIds(products.stream().map(Product::getId).toList());
        return dto;


    }
    public OfferDto getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new OfferNotFoundException("Offer not found"));
        OfferDto dto=new OfferDto();
        dto.setTitle(offer.getTitle());
        dto.setDescription(offer.getDescription());
        if (offer.getProducts() != null) {
            List<Long> productIds = offer.getProducts()
                    .stream()
                    .map(Product::getId)
                    .toList();
            dto.setProductIds(productIds);
        }
        return dto;

    }

    public List<OfferDto> getAllOffers(){
        List<Offer> offers= offerRepository.findAll();
        List<OfferDto> dtoList=new ArrayList<>();
        for(Offer offer:offers){
            OfferDto dto=new OfferDto();
            dto.setTitle(offer.getTitle());
            dto.setDescription(offer.getDescription());
            if(offer.getProducts()!=null){
                List<Long> productIds=offer.getProducts().stream().map(Product::getId).toList();
                dto.setProductIds(productIds);
            }
            dtoList.add(dto);
        }
        return dtoList;

    }
    public OfferDto addProductToOffer(Long offerId,Long productId){
        Offer offer=offerRepository.findById(offerId).orElseThrow(()->new RuntimeException("Offer not found"));
        Product product=productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product not found"));
        if(!offer.getProducts().contains(product)){
            offer.getProducts().add(product);
        }
        Offer savedOffer=offerRepository.save(offer);
        OfferDto dto=new OfferDto();
        dto.setTitle(savedOffer.getTitle());
        dto.setDescription(savedOffer.getDescription());
        dto.setProductIds(savedOffer.getProducts().stream().map(Product::getId).toList());
        return dto;
    }
    public OfferDto removeProductFromOffer(Long offerId, Long productId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        offer.getProducts().remove(product);

        Offer updatedOffer = offerRepository.save(offer);


        OfferDto dto = new OfferDto();
        dto.setTitle(updatedOffer.getTitle());
        dto.setDescription(updatedOffer.getDescription());
        dto.setProductIds(
                updatedOffer.getProducts().stream()
                        .map(Product::getId)
                        .toList()
        );

        return dto;
    }


}
