package org.example.orderservice.repositories;

import org.example.orderservice.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query methods (if needed) can be defined here
    public List<Order> findAllByCustomerId(Long customerId);
}