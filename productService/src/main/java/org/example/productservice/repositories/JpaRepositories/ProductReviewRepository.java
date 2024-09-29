package org.example.productservice.repositories.JpaRepositories;

import org.example.productservice.models.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    // You can add custom queries if needed
    List<ProductReview> findByProductId(Long productId);
}