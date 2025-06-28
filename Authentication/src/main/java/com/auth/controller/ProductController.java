package com.auth.controller;

import com.auth.entities.Product;
import com.auth.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepo productRepo;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getPaginatedProducts(
            @RequestParam(required = false) String category,
            Pageable pageable
    ) {
        Page<Product> page;
        if (category == null || category.equalsIgnoreCase("All")) {
            page = productRepo.findAll(pageable);
        } else {
            page = productRepo.findByCategoryIgnoreCase(category, pageable);
        }
        return ResponseEntity.ok(page);
    }

    @Cacheable("categories")
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = productRepo.findDistinctCategories();
        return ResponseEntity.ok(categories);
    }

}
