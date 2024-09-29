package org.example.orderservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderItem extends BaseModel {


    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order; // Store order ID
    private Long productId; // Store product ID

    private Integer quantity;
    private Double price;
}
