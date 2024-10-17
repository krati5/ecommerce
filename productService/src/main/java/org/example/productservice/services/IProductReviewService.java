package org.example.productservice.services;

import org.example.productservice.dtos.ProductReviewDto;
import org.example.productservice.exceptions.NotFoundException;

import java.util.List;

public interface IProductReviewService {
    ProductReviewDto createReview(ProductReviewDto reviewDto) throws NotFoundException;

    ProductReviewDto getReviewById(Long id) throws NotFoundException;

    List<ProductReviewDto> getAllReviews();

    ProductReviewDto updateReview(Long id, ProductReviewDto reviewDto) throws NotFoundException;

    void deleteReview(Long id) throws NotFoundException;

    List<ProductReviewDto> getReviewsByProduct(Long productId) throws NotFoundException;
}
