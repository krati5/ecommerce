package org.example.cartservice.services;

import jakarta.transaction.Transactional;
import org.example.cartservice.models.Cart;

import java.util.Optional;

public interface ICartService {
    Optional<Cart> getCartByUserId(Long userId);

    @Transactional
    Cart addToCart(Long userId, Long productId, Integer quantity);

    boolean softDeleteCart(Long userId);
}
