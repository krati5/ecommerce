package org.example.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.cartservice.models.Cart;
import org.example.cartservice.models.CartItem;

@Getter
@Setter
public class AddToCartRequestDTO {
    private Long productId;
    private Integer quantity;

}
