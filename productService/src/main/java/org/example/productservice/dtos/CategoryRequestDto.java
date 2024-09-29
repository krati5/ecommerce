package org.example.productservice.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.productservice.models.Category;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class CategoryRequestDto {
    private String name;
    private String description;

    public Category toCategory(){

        Category category = new Category();
        category.setName(this.getName());
        category.setDescription(this.getDescription());
        return category;

    }

    public static CategoryRequestDto fromCategory(Category category) {

        CategoryRequestDto categoryDto = new CategoryRequestDto();
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        return categoryDto;
    }
}
