package com.ehizman.inventorymgt.repository;

import com.ehizman.inventorymgt.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findProductByProductId(String productId);
}
