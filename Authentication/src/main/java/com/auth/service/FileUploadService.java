package com.auth.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileUploadService {


    private  int MAX_IMAGE_UPLOAD_SIZE = 5*1024*1024;

    @Value("${file.upload.image.path}")
    private String IMAGE_UPLOAD_PATH;
    @Value("${file.upload.pdf.path}")
    private String PDF_UPLOAD_PATH;

    public void handleImageUpload(MultipartFile inputImage) throws Exception {

        String filename = StringUtils.cleanPath(inputImage.getOriginalFilename());
        String fileType = StringUtils.getFilenameExtension(filename);
        //System.out.println(fileType);

        String[] allowedTypes = {"png","jpg","jpeg","gif"};

        boolean isAllowedType = Arrays.stream(allowedTypes).anyMatch(type -> type.equalsIgnoreCase(fileType));

        if(!isAllowedType){

            throw  new Exception(fileType +"  is not allowed");
        }

        if( inputImage.getSize()>MAX_IMAGE_UPLOAD_SIZE){
            throw new Exception("Max allowed Size is 5MB");
        }

        String newFileName = UUID.randomUUID().toString()+"."+fileType;

        Path uploadPath = Paths.get(IMAGE_UPLOAD_PATH + newFileName);

        Files.copy(inputImage.getInputStream(),uploadPath);

    }
}
