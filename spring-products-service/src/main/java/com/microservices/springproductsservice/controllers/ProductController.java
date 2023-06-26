package com.microservices.springproductsservice.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.springproductsservice.dtos.ProductRequest;
import com.microservices.springproductsservice.models.Product;
import com.microservices.springproductsservice.services.ProductService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    /* Get all products from db */
    @GetMapping
    public ResponseEntity<List<Product>> getProductsRoute() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    /*
     * Create new product if user is authorized, authorization is checked by user
     * service which returns user id
     */
    @PostMapping
    public ResponseEntity<?> createProductRoute(@Valid @ModelAttribute ProductRequest productRequest,
            BindingResult bindingResult,
            @RequestHeader(value = "Authorization", required = true) String bearerToken) {
        return productService.createProduct(bearerToken, productRequest);
    }

    /* Get single product from db */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSingleProductRoute(@PathVariable UUID id) {
        return productService.getSingleProduct(id);
    }

    /* Get products by ownerId */
    @GetMapping(value = "owner/{id}")
    public ResponseEntity<?> getProductsByOwner(@PathVariable String id) {
        return productService.getProductsByOwner(id);
    }

}
