package org.example.productservice.controllers;

import org.example.productservice.dtos.CategoryRequestDto;
import org.example.productservice.dtos.ProductResponseDto;
import org.junit.jupiter.api.Test;

import org.example.productservice.dtos.CategoryDto;
import org.example.productservice.exceptions.NotFoundException;
import org.example.productservice.models.Category;
import org.example.productservice.models.Product;
import org.example.productservice.services.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories() throws Exception {
        // Arrange
        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        category.setName("Electronics");
        categories.add(category);

        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act
        ResponseEntity<List<CategoryDto>> response = categoryController.getAllCategories();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(categoryService, times(1)).getAllCategories();
    }


    @Test
    void getAllCategories_NotFound() {
        // Arrange
        when(categoryService.getAllCategories()).thenReturn(new ArrayList<>());

        // Act & Assert
        NotFoundException thrownException = assertThrows(NotFoundException.class, () -> {
            categoryController.getAllCategories();
        });

        assertEquals("No categories found.", thrownException.getMessage());

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getProductsByCategory() throws Exception {
        // Arrange
        String categoryName = "Electronics";
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setTitle("Smartphone");
        products.add(product);

        when(categoryService.getProductsInCategory(categoryName)).thenReturn(products);

        // Act
        ResponseEntity<List<ProductResponseDto>> response = categoryController.getProductsByCategory(categoryName);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(categoryService, times(1)).getProductsInCategory(categoryName);
    }

    @Test
    void getProductsByCategory_Exception() throws NotFoundException {
        // Arrange
        String categoryName = "Electronics";
        when(categoryService.getProductsInCategory(categoryName)).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            categoryController.getProductsByCategory(categoryName);
        });

        assertEquals("Unexpected error", thrownException.getMessage());
        verify(categoryService, times(1)).getProductsInCategory(categoryName);


    }

    @Test
    void addCategory() {
        // Arrange
        Category category = new Category();
        category.setName("Electronics");

        when(categoryService.createCategory(CategoryRequestDto.fromCategory(category))).thenReturn(category);

        // Act
        ResponseEntity<CategoryDto> response = categoryController.addCategory(CategoryRequestDto.fromCategory(category));

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getName());
        verify(categoryService, times(1)).createCategory(CategoryRequestDto.fromCategory(category));
    }


    @Test
    void addCategory_Exception() {
        // Arrange
        Category category = new Category();
        category.setName("Electronics");

        when(categoryService.createCategory(CategoryRequestDto.fromCategory(category))).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            categoryController.addCategory(CategoryRequestDto.fromCategory(category));
        });

        assertEquals("Unexpected error", thrownException.getMessage());
        verify(categoryService, times(1)).createCategory(CategoryRequestDto.fromCategory(category));

    }
}