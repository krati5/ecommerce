package org.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String id;
    private String title;
    private String description;
    private Double price;
    private int stockQuantity;
    private String imageUrl;

}
