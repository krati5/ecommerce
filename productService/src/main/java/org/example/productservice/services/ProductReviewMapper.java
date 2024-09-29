
package org.example.productservice.services;

import org.example.productservice.dtos.ProductReviewDto;
import org.example.productservice.models.Product;
import org.example.productservice.models.ProductReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductReviewMapper {

    @Autowired
    private SelfProductService productService;

    public ProductReview toProductReview(ProductReviewDto dto) {
        ProductReview review = new ProductReview();
        review.setId(dto.getId());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setUserId(dto.getUserId());

        // Fetch the product using the productId from the dto
        Product product = productService.getSingleProduct(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        review.setProduct(product);


        return review;
    }
}
