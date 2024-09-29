package org.example.orderservice.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseModel {

    private Long customerId; // Store user ID

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    private OrderStatus orderStatus; // e.g., "PENDING", "COMPLETED", "CANCELLED"
    private Double totalAmount;

    private Long paymentId; // Reference to the Payment in Payment Service

    private Long addressId;
}

