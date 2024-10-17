package org.example.orderservice.services;

import org.example.orderservice.dtos.CartDTO;
import org.example.orderservice.exceptions.AuthenticationException;
import org.example.orderservice.exceptions.InsufficientQuantity;
import org.example.orderservice.exceptions.NotFoundException;
import org.example.orderservice.models.Order;
import org.example.orderservice.models.OrderStatus;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    default void validateCart(CartDTO cart, Long userId) throws NotFoundException {


        // Validate that the cart belongs to the authenticated user
        if (!cart.getUserId().equals(userId)) {
            throw new AuthenticationException("You are not authorized to checkout this cart.");
        }

        // Validate that the cart has product items
        if (cart.getCartItems().isEmpty()) {
            throw new NotFoundException("No items found in the cart with id " + cart.getId());
        }

    }

    Order processCheckout(Authentication authentication) throws NotFoundException, InsufficientQuantity;

    Order createOrder(Order order) throws InsufficientQuantity;

    Order addAddressToOrder(Long orderId, Long addressId) throws NotFoundException;

    Optional<Order> getOrderById(Long id);

    Order updateOrderStatus(Long id, OrderStatus status) throws NotFoundException;

    boolean deleteOrder(Long id);

    List<Order> listOrders();

    List<Order> getOrdersByCustomerId(Long customerId);

    Order updateOrderWithPaymentId(Long orderId, Long paymentId) throws NotFoundException;

    Order processOrderConfirmation(Long orderId) throws NotFoundException;
}
