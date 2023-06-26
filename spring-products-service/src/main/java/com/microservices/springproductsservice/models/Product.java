package com.microservices.springproductsservice.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @NotBlank(message = "name can not be empty")
    @Size(min = 2, message = "{validation.name.size.too_short}")
    @Size(max = 200, message = "{validation.name.size.too_long}")
    private String name;

    private String ownerId;

    @NotBlank(message = "This field can't be empty")
    private String description;

    @NotNull
    @DecimalMin(value = "0.1", message = "Price must be at least 0.1")
    @DecimalMax(value = "99999", message = "Price can't be greater than 99999")
    @Digits(integer = 5, fraction = 2, message = "Price must have at most 2 decimal places")
    private BigDecimal price;

    @NotNull
    @Min(value = 1, message = "Stock can't be less than 1")
    @Max(value = 10000, message = "Stock can't be greater than 10000")
    private Integer stock;

    private String picture_path;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
