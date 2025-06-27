package com.auth.controller;

import com.auth.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/image")
    public ResponseEntity<?> imageUpload(@RequestParam("image_upload") MultipartFile inputImage ) throws Exception {

        fileUploadService.handleImageUpload(inputImage);

        Map<String , Object> response = new HashMap<>();

        response.put("result","Success");
        response.put("message","Image Upload Successful");
        return ResponseEntity.ok(response);
    }
}
