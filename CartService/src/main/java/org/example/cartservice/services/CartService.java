package org.example.cartservice.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.cartservice.exceptions.CartNotFoundException;
import org.example.cartservice.models.Cart;
import org.example.cartservice.models.CartItem;
import org.example.cartservice.repositories.CartItemRepository;
import org.example.cartservice.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Optional<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserIdAndIsDeletedFalse(userId);

    }
    @Override
    @Transactional
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        // Find the cart by userId
        Optional<Cart> cartOptional = getCartByUserId(userId);

        Cart cart;
        if (cartOptional.isPresent()) {
            cart = cartOptional.get();
        } else {
            // If cart does not exist, create a new one
            cart = new Cart();
            cart.setUserId(userId);
            cart.setCartItems(new ArrayList<>());
            cart = cartRepository.save(cart);
        }

        // Check if the product is already in the cart
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // If product is already in the cart, update the quantity
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // If product is not in the cart, add it as a new CartItem
            CartItem newCartItem = new CartItem();
            newCartItem.setProductId(productId);
            newCartItem.setQuantity(quantity);
            newCartItem.setCart(cart);

            cart.getCartItems().add(newCartItem);
        }

        return cartRepository.save(cart);
    }

    @Override
    public boolean softDeleteCart(Long userId) {
        Optional<Cart> cartOptional = getCartByUserId(userId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.setDeleted(true);
            cartRepository.save(cart);
            return true;
        } else {
            throw new CartNotFoundException("Cart not found for userId: " + userId);
        }
    }

}
