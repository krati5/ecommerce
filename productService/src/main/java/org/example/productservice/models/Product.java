package org.example.productservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
// for elastic search
@Document(indexName = "products")
@Entity
public class Product extends BaseModel {
    private String title;
    private String description;
    private Double price;
    private Integer stockQuantity;
    // 1 product can have 1 category and 1 category can have multiple products
    // P : C is m:1 cardinatly
    @ManyToOne
    @JsonBackReference
    @Field(type = FieldType.Nested, includeInParent = true)
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<ProductReview> reviews;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<ProductSpecification> specifications;

    private String imageUrl;
}
