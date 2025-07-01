package com.example.minioproject.service;

import com.example.minioproject.dto.ProductDto;
import com.example.minioproject.entity.Offer;
import org.springframework.data.domain.Pageable;
import com.example.minioproject.entity.Product;
import com.example.minioproject.entity.User;
import com.example.minioproject.enums.ProductStatus;
import com.example.minioproject.repository.OfferRepository;
import com.example.minioproject.repository.ProductRepository;
import com.example.minioproject.repository.UserRepository;
import io.minio.MinioClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OfferRepository offerRepository;
    private final MinioClient minioClient;
    private final String bucketName="product-images";
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public void createProduct(ProductDto productDto, MultipartFile imageFile){
        try{
            String imageName= UUID.randomUUID()+"-"+imageFile.getOriginalFilename();
            InputStream inputStream=imageFile.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imageName)
                            .stream(inputStream,imageFile.getSize(),-1)
                            .contentType(imageFile.getContentType())
                            .build()
            );
            User user=userRepository.findById(productDto.getOwnerId()).orElseThrow(()->new RuntimeException("User not Found"));


            Product product=new Product();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setStatus(productDto.getStatus());
            product.setImageUrl("/"+bucketName+"/"+imageName);
            product.setOwner(user);
            if(productDto.getOfferIds()!=null){
                List<Offer> offers=offerRepository.findAllById(productDto.getOfferIds()).stream().collect(Collectors.toList());
                product.setOffers(offers);
            }
            productRepository.save(product);
        }catch(Exception e){
            throw new RuntimeException("Error uploading image to MinIo",e);

        }
    }
    public void updateProduct(Long id,ProductDto productDto,MultipartFile imageFile){
        Product product=productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not Found"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setStatus(productDto.getStatus());
        User user=userRepository.findById(productDto.getOwnerId()).orElseThrow(()->new RuntimeException("User not Found"));
        product.setOwner(user);
        if(productDto.getOfferIds()!=null){
            List<Offer> offers=offerRepository.findAllById(productDto.getOfferIds()).stream().collect(Collectors.toList());
            product.setOffers(offers);
        }
        if(imageFile!=null && !imageFile.isEmpty()){
            String newImageName=UUID.randomUUID()+"-"+imageFile.getOriginalFilename();
            try (InputStream in = imageFile.getInputStream()) {

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(newImageName)
                                .stream(in, imageFile.getSize(), -1)
                                .contentType(imageFile.getContentType())
                                .build()
                );

                product.setImageUrl("/" + bucketName + "/" + newImageName);

            } catch (Exception e) {
                throw new RuntimeException(
                        "Error uploading new image to MinIO", e);
            }



        }
        productRepository.save(product);



    }
    public ProductDto getProduct(Long id){
        Product product=productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
        ProductDto dto= modelMapper.map(product,ProductDto.class);
        if(product.getOffers()!=null){
            List<Long> offerIds=product.getOffers().stream().map(Offer::getId).toList();
            dto.setOfferIds(offerIds);

        }
        return dto;

    }
    public List<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, Sort.by("id").ascending());
        Page<Product> pageProduct = productRepository.findAll(pageable);
        List<Product> allProducts = pageProduct.getContent();

        List<ProductDto> dtoList = new ArrayList<>();
        for (Product product : allProducts) {
            ProductDto dto = modelMapper.map(product, ProductDto.class);

            if (product.getOffers() != null) {
                List<Long> offerIds = product.getOffers()
                        .stream()
                        .map(Offer::getId)
                        .toList();
                dto.setOfferIds(offerIds);
            }

            dtoList.add(dto);
        }

        return dtoList;
    }

    public void changeStatus(Long id, ProductStatus status){
        Product product=productRepository.findById(id).orElseThrow(()->new RuntimeException("Product not found"));
        product.setStatus(status);
        productRepository.save(product);
    }
    public List<ProductDto> getProductByStatusJPQL(ProductStatus status) {

        List<Product> products = productRepository.findProductsByStatus(status);
        List<ProductDto> dtos=new ArrayList<>();
        for(Product p : products){
            ProductDto dto=modelMapper.map(p,ProductDto.class);
            if(p.getOffers()!=null){
                List<Long> offerIds=p.getOffers().stream().map(Offer::getId).toList();
                dto.setOfferIds(offerIds);
            }
            dtos.add(dto);
        }
        return dtos;

    }

    public List<ProductDto> getProductByStatusNative(ProductStatus status) {

        List<Product> products =
                productRepository.findProductsByStatusNative(status.name());

        List<ProductDto> dtos = new ArrayList<>();

        for (Product p : products) {
            ProductDto dto = modelMapper.map(p, ProductDto.class);

            if (p.getOffers() != null) {
                List<Long> offerIds = p.getOffers()
                        .stream()
                        .map(Offer::getId)
                        .toList();
                dto.setOfferIds(offerIds);
            }

            dtos.add(dto);
        }

        return dtos;
    }



}
