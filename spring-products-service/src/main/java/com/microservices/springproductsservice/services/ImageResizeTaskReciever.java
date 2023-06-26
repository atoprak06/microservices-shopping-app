package com.microservices.springproductsservice.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.microservices.springproductsservice.models.TaskStatus;
import com.microservices.springproductsservice.repository.TaskStatusRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageResizeTaskReciever {
    private final TaskStatusRepository taskStatusRepository;

    @RabbitListener(queues = "imageResizeQueue", concurrency = "1")
    public void recieveImageResizeTask(ImageResizeTask task) {

        TaskStatus taskStatus = TaskStatus.builder().status("IN_PROGRESS").date_done(LocalDateTime.now()).build();
        taskStatusRepository.save(taskStatus);

        String imagePath = task.getImagePath();
        Integer targetWidth = task.getTartgetWidth();
        Integer targetHeight = task.getTargetHeight();
        String fileExtension;

        Path path = Paths.get(imagePath);
        int dotIndex = imagePath.lastIndexOf(".");

        try {
            if (dotIndex >= 0 && dotIndex < imagePath.length() - 1) {
                // Extract the file extension
                fileExtension = imagePath.substring(dotIndex + 1);
                BufferedImage sourceImage = ImageIO.read(path.toFile());
                BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics = resizedImage.createGraphics();
                graphics.drawImage(sourceImage, 0, 0, targetWidth, targetHeight, null);
                graphics.dispose();

                ImageIO.write(resizedImage, fileExtension, path.toFile());

                taskStatus.setStatus("SUCCESS");
                taskStatus.setDate_done(LocalDateTime.now());
                taskStatusRepository.save(taskStatus);
            } else {
                System.out.println("Invalid file name.");
                taskStatus.setStatus("FAILURE");
                taskStatus.setDate_done(LocalDateTime.now());
                taskStatusRepository.save(taskStatus);
            }

        } catch (Exception e) {
            e.printStackTrace();
            taskStatus.setStatus("FAILURE");
            taskStatus.setDate_done(LocalDateTime.now());
            taskStatusRepository.save(taskStatus);
        }

    }

}
