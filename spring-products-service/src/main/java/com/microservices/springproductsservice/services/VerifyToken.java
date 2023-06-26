package com.microservices.springproductsservice.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VerifyToken {
    private final WebClient webClient;

    public ResponseEntity<String> VerifyTokenResponse(String bearerToken) {

        String ownerId;
        try {
            ownerId = webClient.get()
                    .uri("http://localhost:8000/auth/verify-token")
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                System.out.println(e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(ownerId);
    }

}
