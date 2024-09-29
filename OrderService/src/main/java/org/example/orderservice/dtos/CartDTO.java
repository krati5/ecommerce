package org.example.orderservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> cartItems;

}
