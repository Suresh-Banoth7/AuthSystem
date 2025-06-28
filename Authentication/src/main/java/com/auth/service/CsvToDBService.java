package com.auth.service;

import com.auth.entities.Product;
import com.auth.repository.ProductRepo;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvToDBService {


    @Autowired
    private CsvToDbProcessBatchService csvToDbProcessBatchService;

    @Autowired
    private ProductRepo productRepo;

    public void handleCsvToDBUpload(MultipartFile inputFile) throws IOException, CsvValidationException {

        String filename = StringUtils.cleanPath(inputFile.getOriginalFilename());
        String fileType = StringUtils.getFilenameExtension(filename);

        if(!fileType.equals("csv")){
            throw new RuntimeException(fileType+"  is not Allowed");
        }

      Reader reader = new BufferedReader(new InputStreamReader(inputFile.getInputStream()));

        CSVReader csvReader = new CSVReader(reader);

        String[] csvRow = csvReader.readNext();
        csvRow = csvReader.readNext();

        List<Product> productList = new ArrayList<>();
        while((csvRow=csvReader.readNext())!=null){

            Product product = new Product();
            product.setProductID(csvRow[0]);
            product.setTitle(csvRow[1]);
            product.setDescription(csvRow[2]);
            product.setCategory(csvRow[3]);
            product.setImageUrl(csvRow[4]);
            product.setPrice(Float.parseFloat(csvRow[5]));
            product.setInStock(Boolean.parseBoolean(csvRow[6]));
            product.setRating(Float.parseFloat(csvRow[7]));
            productList.add(product);
           if(productList.size() ==1000){
               csvToDbProcessBatchService.csvToDbProcessBatchUpload(new ArrayList<>(productList));

               productList.clear();
           }

        }

        if(!productList.isEmpty()){

            csvToDbProcessBatchService.csvToDbProcessBatchUpload(new ArrayList<>(productList));
        }

    }


}
