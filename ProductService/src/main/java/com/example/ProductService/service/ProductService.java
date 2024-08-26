package com.example.ProductService.service;

import com.example.ProductService.dto.ProductRequest;
import com.example.ProductService.dto.ProductResponse;
import com.example.ProductService.model.Products;
import com.example.ProductService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor   //this will automatically do constructor injection.
public class ProductService {

    private final ProductRepository productRepository;

    public void CreateProduct(ProductRequest productRequest){
        Products products = Products.builder()
                .name(productRequest.getProduct_name())
                .description(productRequest.getProduct_desc())
                .price(productRequest.getProduct_price())
                .build();

        productRepository.save(products);
        log.info("Product with id {} is saved.",products.getId());
    }

    public List<ProductResponse> getAllProducts(){
         List<Products> productsList = productRepository.findAll();
         //for mapping products with ProductResponse
        return productsList.stream().map(products -> mapToProductResponse(products)).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Products products){
        return ProductResponse.builder()
                .productId(products.getId())
                .productName(products.getName())
                .productDescription(products.getDescription())
                .productPrice(products.getPrice())
                .build();
    }
}
