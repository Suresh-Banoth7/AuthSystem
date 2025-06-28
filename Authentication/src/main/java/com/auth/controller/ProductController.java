package com.auth.controller;

import com.auth.entities.Product;
import com.auth.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Product> pageResult;

        if (category == null || category.equalsIgnoreCase("All")) {
            pageResult = productRepo.findAll(pageable);
        } else {
            pageResult = productRepo.findByCategoryIgnoreCase(category, pageable);
        }
        return ResponseEntity.ok(pageResult);
    }


    @Cacheable("categories")
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = productRepo.findDistinctCategories();
        return ResponseEntity.ok(categories);
    }

}
