package com.cliff.backend.rest;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cliff.backend.rest.CustomVision;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;



@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")
public class Service {
    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }

    @PostMapping(value = "/images")
    public ResponseEntity<Object> upload(@RequestBody String data) throws IOException, JSONException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        String imageName = UUID.randomUUID() + ".png";
        saveImageToTraining(decode);
         saveImageToFile(decode, imageName);
//         saveToCloud(decode, imageName);
        return new ResponseEntity<Object>("Image saved sucessfully", HttpStatus.OK);
    }
    @PostMapping(value = "/validate")
    public ResponseEntity<String> validate(@RequestBody String data) throws IOException, JSONException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] decode = Base64.getDecoder().decode(base64);
        ResponseEntity<String> result = CustomVision.validate(decode);
        System.out.print(result);
        return result;
//        return CustomVision.validate(decode);
    }

    public void saveImageToTraining(byte[] image) throws  IOException{
        CustomVision.uploadImage(CustomVision.tagIdCliff,image);
    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File path = new File("./images/");
        if (!path.exists()) {
            path.mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);

    }
//
//    private void saveToCloud(byte[] image, String imageName) {
//        // Retrieve the connection string for use with the application.
//        String connectStr = "DefaultEndpointsProtocol=https;AccountName=cliff;AccountKey=mj0VXCDPYlqcHYLIu9h4DoaKAFPSjBZVhJq6iaarR9adijJOhrdbQv+g0RkfMNFRCiUqzSlGhCkB+AStARghfQ==;EndpointSuffix=core.windows.net";
//        // Create a BlobServiceClient object using a connection string
//        BlobServiceClient client = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();
//
//        // Create a unique name for the container
//        String containerName = "images";
//
//        // Create the container and return a container client object
//        BlobContainerClient blobContainerClient = client.createBlobContainerIfNotExists(containerName);
//
//        // Get a reference to a blob
//        BlobClient blobClient = blobContainerClient.getBlobClient(imageName);
//
//        // Upload the blob
//        // blobClient.uploadFromFile(localPath + fileName);
//        InputStream targetStream = new ByteArrayInputStream(image);
//        blobClient.upload(targetStream);
//    }
}


