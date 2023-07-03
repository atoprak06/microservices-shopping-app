package com.microservices.springproductsservice.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UploadImage {
    private final ImageResizeTaskSender imageResizeTaskSender;

    public Boolean isValidFile(MultipartFile file) {
        String contentType = file.getContentType();
        List<String> validTypes = List.of("image/png", "image/jpg", "image/jpeg");
        if (validTypes.contains(contentType)) {
            return true;
        }
        return false;
    }

    public String createRandomName(String original_name) {
        Random random = new Random();
        int min = 100000000;
        int max = 999999999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        String pictureName = String.valueOf(randomNumber) + "-" + original_name;
        return pictureName;
    }

    public ResponseEntity<String> uploadPicture(MultipartFile file) {
        if (isValidFile(file)) {
            try {
                String picture_name = createRandomName(file.getOriginalFilename());

                // Get root folder path
                Path rootPath = Paths.get("");
                String rootFolderPath = rootPath.toAbsolutePath().toString();
                // Create the destination directory if it doesn't exist
                String targetPath = rootFolderPath + "/src/main/resources/static/uploads";
                File directory = new File(
                        targetPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File destinationFile = new File(
                        targetPath + "/" + picture_name);
                file.transferTo(destinationFile);
                imageResizeTaskSender.enqueueImageResizeTask(ImageResizeTask.builder()
                        .imagePath(targetPath + "/" + picture_name).targetHeight(600).tartgetWidth(600).build());
                return ResponseEntity.status(HttpStatus.CREATED).body(picture_name);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
            }

        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid file extension");

    }

}
