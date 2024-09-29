package org.example.productservice.services;

import org.example.productservice.dtos.CategoryRequestDto;
import org.example.productservice.exceptions.NotFoundException;
import org.example.productservice.models.Category;
import org.example.productservice.models.Product;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Long id);

    Category createCategory(CategoryRequestDto categoryDto);

    Category updateCategory(Long id, CategoryRequestDto categoryDto) throws NotFoundException;

    void deleteCategory(Long id);

    //
//    Optional<Category> getCategory(Long categoryId);
//
//    Category addCategory(Category category);
//    Category updateCategory(Long categoryId, Category category);
//
//    Category deleteCategory(Long categoryId);
//
    List<Product> getProductsInCategory(String categoryName) throws NotFoundException;

}
