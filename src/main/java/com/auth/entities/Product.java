package com.auth.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_price", columnList = "price")
})
@Data
public class Product {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

     private String productID;

     private String title;

     private String description;

     private String category;

     private String imageUrl;

     private float price;

     private boolean inStock;

     private float rating;


}
