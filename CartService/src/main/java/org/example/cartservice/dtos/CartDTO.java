package org.example.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.cartservice.models.Cart;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> cartItems;

    // Convert Cart Entity to Response DTO
    public static CartDTO fromCartEntity(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(cart.getUserId());
        cartDTO.setId(cart.getId());

        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
                .map(cartItem -> {
                    CartItemDTO cartItemDTO = new CartItemDTO();
                    cartItemDTO.setId(cartItem.getId());
                    cartItemDTO.setProductId(cartItem.getProductId());
                    cartItemDTO.setQuantity(cartItem.getQuantity());
                    return cartItemDTO;
                }).collect(Collectors.toList());

        cartDTO.setCartItems(cartItemDTOs);
        return cartDTO;
    }
}
