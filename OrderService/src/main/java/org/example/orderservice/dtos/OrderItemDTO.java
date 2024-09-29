package org.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.orderservice.models.OrderItem;

@Getter
@Setter
public class OrderItemDTO {
    private Long id;
    private Long productId;  // Product ID
    private int quantity;  // Quantity ordered
    private Double price;


    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setProductId(orderItem.getProductId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());

        return orderItemDTO;
    }
}