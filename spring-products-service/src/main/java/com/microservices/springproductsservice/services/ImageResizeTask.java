package com.microservices.springproductsservice.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResizeTask {
    private String imagePath;
    private int tartgetWidth;
    private int targetHeight;    
}
