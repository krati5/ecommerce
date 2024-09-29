//package org.example.productservice.services;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.example.productservice.exceptions.NotFoundException;
//import org.example.productservice.models.Category;
//import org.example.productservice.models.Product;
//import org.example.productservice.repositories.JpaRepositories.CategoryRepository;
//import org.example.productservice.repositories.JpaRepositories.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class SelfCategoryServiceTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private SelfCategoryService selfCategoryService;
//
//    private Category category;
//    private Product product;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        product = new Product();
//        product.setId(1L);
//        product.setTitle("Test Product");
//        product.setPrice(100.0);
//
//        category = new Category();
//        category.setId(1L);
//        category.setName("Electronics");
//        category.setDescription("Category for electronics");
//        category.setProducts(Collections.singletonList(product));
//    }
//
//    @Test
//    void getAllCategories_Success() {
//        // Arrange
//        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
//
//        // Act
//        List<Category> result = selfCategoryService.getAllCategories();
//
//        // Assert
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.size());
//        assertEquals("Electronics", result.get(0).getName());
//        verify(categoryRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getAllCategories_EmptyList() {
//        // Arrange
//        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
//
//        // Act
//        List<Category> result = selfCategoryService.getAllCategories();
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//        verify(categoryRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getProductsInCategory_Success() throws NotFoundException {
//        // Arrange
//        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));
//
//        // Act
//        List<Product> result = selfCategoryService.getProductsInCategory("Electronics");
//
//        // Assert
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.size());
//        assertEquals("Test Product", result.get(0).getTitle());
//        verify(categoryRepository, times(1)).findByName("Electronics");
//    }
//
//    @Test
//    void getProductsInCategory_CategoryNotFound() {
//        // Arrange
//        when(categoryRepository.findByName("NonExistentCategory")).thenReturn(Optional.empty());
//
//        // Act & Assert
//        NotFoundException thrown = assertThrows(NotFoundException.class, () ->
//                selfCategoryService.getProductsInCategory("NonExistentCategory"));
//        assertEquals("Category with name NonExistentCategory not found.", thrown.getMessage());
//        verify(categoryRepository, times(1)).findByName("NonExistentCategory");
//    }
//
//    @Test
//    void addCategory_Success() {
//        // Arrange
//        when(categoryRepository.save(category)).thenReturn(category);
//
//        // Act
//        Category result = selfCategoryService.addCategory(category);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("Electronics", result.getName());
//        verify(categoryRepository, times(1)).save(category);
//    }
//
//    @Test
//    void addCategory_Exception() {
//        // Arrange
//        when(categoryRepository.save(category)).thenThrow(new RuntimeException("Unexpected error"));
//
//        // Act & Assert
//        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
//                selfCategoryService.addCategory(category));
//        assertEquals("Unexpected error", thrown.getMessage());
//        verify(categoryRepository, times(1)).save(category);
//    }
//}
