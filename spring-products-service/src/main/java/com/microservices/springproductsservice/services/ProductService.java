package com.microservices.springproductsservice.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservices.springproductsservice.dtos.ProductRequest;
import com.microservices.springproductsservice.models.Product;
import com.microservices.springproductsservice.repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final VerifyToken verifyToken;
    private final UploadImage uploadImage;

    /*
     * Create new product if user is authorized, authorization is checked by user
     * service which returns user id
     */
    public ResponseEntity<?> createProduct(String bearerToken, ProductRequest productRequest) {

        ResponseEntity<String> response = verifyToken.VerifyTokenResponse(bearerToken);
        String responseBody = response.getBody();
        HttpStatusCode responseStatusCode = response.getStatusCode();
        MultipartFile picture = null;

        if (responseStatusCode == HttpStatus.OK && responseBody != null) {

            productRequest.setOwnerId(responseBody.replaceAll("\"", ""));

            if (!productRequest.getPicture().isEmpty()) {
                picture = productRequest.getPicture();
            }

            Product new_product = Product.builder().name(productRequest.getName()).price(productRequest.getPrice())
                    .description(productRequest.getDescription()).ownerId(productRequest.getOwnerId())
                    .created_at(LocalDateTime.now()).updated_at(LocalDateTime.now()).stock(productRequest.getStock())
                    .build();

            if (picture != null) {
                ResponseEntity<String> picture_path_response = uploadImage.uploadPicture(picture);
                if (picture_path_response.getStatusCode() == HttpStatus.CREATED) {
                    String picture_name = picture_path_response.getBody();
                    new_product.setPicture_path(picture_name);
                } else {
                    return picture_path_response;
                }
            }

            Product created_product = productRepository.save(new_product);
            log.info("new product is created {}", created_product);
            return ResponseEntity.status(HttpStatus.CREATED).body(created_product);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid authorization header");
    }

    /* Get all products from db */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /* Get single product from db */
    public ResponseEntity<?> getSingleProduct(UUID id) {

        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product is not found");
    }

    /* Get products by ownerId */
    public ResponseEntity<?> getProductsByOwner(String id) {
        Optional<List<Product>> products = productRepository.findByOwnerId(id);
        if (products.isPresent() && !products.get().isEmpty()) {
            System.out.println(products.isPresent());
            return ResponseEntity.status(HttpStatus.OK).body(products);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This user has no products yet");
    }

    /* Delete product if authorized and owner */
    public ResponseEntity<?> deleteProductById(UUID id, String bearerToken) {
        Optional<Product> product = productRepository.findById(id);
        ResponseEntity<String> response = verifyToken.VerifyTokenResponse(bearerToken);
        if (product.isPresent() && response.getStatusCode() == HttpStatus.OK) {
            String ownerOfProduct = product.get().getOwnerId();
            String requestOwner = response.getBody().replace("\"", "");
            if (requestOwner.equals(ownerOfProduct)) {
                productRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product is deleted");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
            }
        } else {
            if (!product.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with this id not found: " + id);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid authorization header");

        }
    }
}