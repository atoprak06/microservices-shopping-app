package com.microservices.springproductsservice.dtos;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private List<String> errors;
}