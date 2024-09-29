package org.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.orderservice.models.Order;
import org.example.orderservice.models.OrderItem;
import org.example.orderservice.models.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderDTO {
    private Long id;
    private Long customerId;  // Assumes that orders are linked to a customer
    private List<OrderItemDTO> items;  // List of order items
    private String status;  // Order status (PENDING, COMPLETED, etc.)
    private Double totalAmount;
    private Long paymentId;
    private Long addressId;


    public Order toOrder(){
        // Convert OrderDTO to Order and OrderItemDTO to OrderItem
        Order order = new Order();
        order.setCustomerId(this.getCustomerId());
        order.setTotalAmount(this.getTotalAmount());
        order.setPaymentId(getPaymentId());
        order.setAddressId(this.getAddressId());

        // Convert status string to OrderStatus enum
        try {
            OrderStatus status = OrderStatus.valueOf(this.getStatus());
            order.setOrderStatus(status);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order status");
        }


        List<OrderItem> orderItems = this.getItems().stream().map(itemDTO -> {
            OrderItem item = new OrderItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            return item;
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        return order;
    }

    public static OrderDTO toOrderDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setCustomerId(order.getCustomerId());
        orderDTO.setStatus(String.valueOf(order.getOrderStatus()));
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setPaymentId(order.getPaymentId());
        orderDTO.setAddressId(order.getAddressId());

        // Convert list of OrderItem entities to DTOs
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(OrderItemDTO::toOrderItemDTO)
                .collect(Collectors.toList());
        orderDTO.setItems(itemDTOs);

        return orderDTO;
    }
}