package com.auth.service;

import com.auth.entities.Product;
import com.auth.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.List;

@Service
public class CsvToDbProcessBatchService {

    @Autowired
    private ProductRepo productRepo;

    @Async("csvAsyncExecutor")
    public void csvToDbProcessBatchUpload(List<Product> productList){

        productRepo.saveAll(productList);
        System.out.println(Thread.currentThread().getName());

    }
}
