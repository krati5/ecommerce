package org.example.productservice.dtos;

import lombok.*;
import org.example.productservice.models.Product;
import org.example.productservice.models.ProductReview;
import org.example.productservice.services.SelfProductService;

import java.util.Optional;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDto {
    private Long id;
    private int rating;
    private String comment;
    private Long productId;
    private Long userId;

    public static ProductReviewDto fromProductReview(ProductReview review) {
        ProductReviewDto dto = new ProductReviewDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setProductId(review.getProduct().getId());
        dto.setUserId(review.getUserId());
        return dto;
    }

}
