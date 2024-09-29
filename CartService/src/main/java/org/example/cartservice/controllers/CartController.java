package org.example.cartservice.controllers;

import org.example.cartservice.dtos.AddToCartRequestDTO;
import org.example.cartservice.dtos.CartDTO;
import org.example.cartservice.exceptions.CartNotFoundException;
import org.example.cartservice.exceptions.JwtAuthenticationException;
import org.example.cartservice.models.Cart;
import org.example.cartservice.services.CartService;
import org.example.cartservice.services.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user")
    public ResponseEntity<CartDTO> getCartByUserId(Authentication authentication) {

        Long userId = JwtUtils.extractUserIdClaim(authentication);

        Optional<Cart> cartOptional = cartService.getCartByUserId(userId);
        if (cartOptional.isPresent()) {
            return ResponseEntity.ok(CartDTO.fromCartEntity(cartOptional.get()));
        } else {
            throw new CartNotFoundException("Cart not found for userId: " + userId);
        }

        }

    @PostMapping("")
    public ResponseEntity<CartDTO> addToCart(@RequestBody AddToCartRequestDTO request, Authentication authentication) {
        Long userId = JwtUtils.extractUserIdClaim(authentication);
        Cart updatedCart = cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(CartDTO.fromCartEntity(updatedCart));
    }

    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteCart(Authentication authentication) {
        Long userId = JwtUtils.extractUserIdClaim(authentication);
        return ResponseEntity.ok(cartService.softDeleteCart(userId));
    }

}
