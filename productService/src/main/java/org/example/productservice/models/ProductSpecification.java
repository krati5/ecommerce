package org.example.productservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class ProductSpecification extends BaseModel{
    @ManyToOne
    @JsonBackReference
    private Product product;

    private String attributeName;
    private String attributeValue;
}
