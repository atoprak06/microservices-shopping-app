package com.microservices.springproductsservice.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.microservices.springproductsservice.models.Product;
import com.microservices.springproductsservice.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getSingleProductTest() {

        // Given. Arrange inputs and targets. Define behavior of Mock object.
        /*
         * "id": "5dd4235e-8a8f-428b-b7b9-037f706787c7",
         * "name": "asd",
         * "ownerId": "64982838681b764cc4ba9956",
         * "description": "asd",
         * "price": 123.00,
         * "stock": 123,
         * "picture_path": "939881051-pexels-helena-lopes-1388069.jpg",
         * "created_at": "2023-06-30T13:50:47.352792",
         * "updated_at": "2023-06-30T13:50:47.352833"
         */
        Product p = new Product();
        p.setId(UUID.fromString("5dd4235e-8a8f-428b-b7b9-037f706787c7"));
        p.setCreated_at(LocalDateTime.parse("2023-06-30T13:50:47.352833", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        p.setUpdated_at(LocalDateTime.parse("2023-06-30T13:50:47.352833", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        p.setName("asd");
        p.setOwnerId("64982838681b764cc4ba9956");
        p.setDescription("asd");
        p.setPrice(new BigDecimal(123));
        p.setStock(123);
        p.setPicture_path("939881051-pexels-helena-lopes-1388069.jpg");

        when(productRepository.findById(UUID.fromString("5dd4235e-8a8f-428b-b7b9-037f706787c7")))
                .thenReturn(Optional.of(p));

        // When. Act on the target behavior. When steps should cover the method to be
        // tested.
        ResponseEntity<?> returnedP = productService
                .getSingleProduct(UUID.fromString("5dd4235e-8a8f-428b-b7b9-037f706787c7"));

        // Then. Assert expected outcomes.
        assertThat(returnedP.getBody(), Matchers.equalTo(Optional.ofNullable(p)));

    }

}
