package com.example.ProductService.repository;

import com.example.ProductService.model.Products;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Products,String> {

}
