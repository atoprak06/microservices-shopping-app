package com.microservices.springproductsservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.springproductsservice.models.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    public Optional<List<Product>> findByOwnerId(String id);
}
