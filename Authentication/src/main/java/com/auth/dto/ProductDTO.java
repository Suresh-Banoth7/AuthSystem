package com.auth.dto;


import lombok.Data;

@Data
public class ProductDTO {
    private String productID;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private float price;
    private boolean inStock;
    private float rating;

    // Constructors, Getters, Setters
}

