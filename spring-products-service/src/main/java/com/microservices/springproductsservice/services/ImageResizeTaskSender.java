package com.microservices.springproductsservice.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageResizeTaskSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("imageResizeQueue")
    private String queueName;

    public void enqueueImageResizeTask(ImageResizeTask task) {
        rabbitTemplate.convertAndSend(queueName, task);
    }
}
