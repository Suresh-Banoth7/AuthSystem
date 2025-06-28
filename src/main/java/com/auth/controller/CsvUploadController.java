package com.auth.controller;

import com.auth.service.CsvToDBService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/csv")
public class CsvUploadController {

    @Autowired
    private CsvToDBService csvToDBService;

    @PostMapping("/input-file")
    public ResponseEntity<?> csvFileUpload(@RequestParam("csv_file")MultipartFile inputFile) throws IOException, CsvValidationException {

        csvToDBService.handleCsvToDBUpload(inputFile);
        Map<String, Object> responeMap = new HashMap<>();

        responeMap.put("result","Success");
        responeMap.put("message","csv file uploaded successfully");

        return ResponseEntity.ok(responeMap);
    }


}
