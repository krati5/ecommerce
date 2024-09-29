package org.example.productservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
//@Document(indexName = "product_reviews")
public class ProductReview extends BaseModel {
    private int rating;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    private Long userId;
}