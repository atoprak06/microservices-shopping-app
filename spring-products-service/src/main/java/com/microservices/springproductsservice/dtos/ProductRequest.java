package com.microservices.springproductsservice.dtos;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private String ownerId;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private MultipartFile picture = null;
}
