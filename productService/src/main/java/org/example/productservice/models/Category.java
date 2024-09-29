package org.example.productservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseModel  {
    private String name;
    private String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private List<Product> products  = new ArrayList<>();
}
