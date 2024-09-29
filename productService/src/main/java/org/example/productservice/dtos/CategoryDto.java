package org.example.productservice.dtos;

import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.productservice.models.Category;
import org.example.productservice.models.Product;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private List<ProductDto> products;

    public Category toCategory(){

        Category category = new Category();
        category.setId(this.getId());
        category.setName(this.getName());
        category.setDescription(this.getDescription());
        category.setProducts(this.getProducts().stream()
                .map(ProductDto::toProduct)
                .collect(Collectors.toList()));
        return category;

    }

    public static CategoryDto fromCategory(Category category) {

        List<ProductDto> productDtos = category.getProducts().stream()
                .map(ProductDto::fromProduct)
                .collect(Collectors.toList());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setProducts(productDtos);

        return categoryDto;
    }

}
