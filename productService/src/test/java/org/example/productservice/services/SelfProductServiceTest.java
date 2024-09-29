//package org.example.productservice.services;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import org.example.productservice.exceptions.NotFoundException;
//import org.example.productservice.models.Category;
//import org.example.productservice.models.Product;
//import org.example.productservice.repositories.JpaRepositories.CategoryRepository;
//import org.example.productservice.repositories.JpaRepositories.ProductRepository;
//import org.example.productservice.services.SelfProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//class SelfProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private SelfProductService selfProductService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getAllProducts() {
//        // Given
//        Product product1 = new Product();
//        Product product2 = new Product();
//        List<Product> products = Arrays.asList(product1, product2);
//        when(productRepository.findAll()).thenReturn(products);
//
//        // When
//        List<Product> result = selfProductService.getAllProducts();
//
//        // Then
//        assertEquals(products, result);
//        verify(productRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getSingleProduct_Found() {
//        // Given
//        Long productId = 1L;
//        Product product = new Product();
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//
//        // When
//        Optional<Product> result = selfProductService.getSingleProduct(productId);
//
//        // Then
//        assertTrue(result.isPresent());
//        assertEquals(product, result.get());
//        verify(productRepository, times(1)).findById(productId);
//    }
//
//    @Test
//    void getSingleProduct_NotFound() {
//        // Given
//        Long productId = 1L;
//        when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        // When
//        Optional<Product> result = selfProductService.getSingleProduct(productId);
//
//        // Then
//        assertFalse(result.isPresent());
//        verify(productRepository, times(1)).findById(productId);
//    }
//
//    @Test
//    void addProduct() {
//        // Given
//        Product product = new Product();
//        Category category = new Category();
//        category.setName("Test category");
//        product.setCategory(category);
//
//        Category savedCategory = new Category();
//        savedCategory.setName("Test category");
//        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(savedCategory));
//        when(productRepository.save(product)).thenReturn(product);
//
//        // When
//        Product result = selfProductService.addProduct(product);
//
//        // Then
//        assertEquals(product, result);
//        verify(categoryRepository, times(1)).findByName(category.getName());
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void updateProduct_Success() throws NotFoundException {
//        // Given
//        Long productId = 1L;
//        Product product = new Product();
//        product.setId(productId);
//        product.setTitle("Updated Title");
//
//        Product existingProduct = new Product();
//        existingProduct.setId(productId);
//
//        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
//        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
//
//        // When
//        Product result = selfProductService.updateProduct(productId, product);
//
//        // Then
//        assertEquals(existingProduct, result);
//        assertEquals("Updated Title", result.getTitle());
//        verify(productRepository, times(1)).findById(productId);
//        verify(productRepository, times(1)).save(existingProduct);
//    }
//
//    @Test
//    void updateProduct_NotFound() {
//        // Given
//        Long productId = 1L;
//        Product product = new Product();
//        when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(NotFoundException.class, () -> selfProductService.updateProduct(productId, product));
//        verify(productRepository, times(1)).findById(productId);
//    }
//
//    @Test
//    void replaceProduct_Success() throws NotFoundException {
//        // Given
//        Long productId = 1L;
//        Product product = new Product();
//        product.setId(productId);
//
//        Product existingProduct = new Product();
//        existingProduct.setId(productId);
//
//        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
//        when(productRepository.save(existingProduct)).thenReturn(existingProduct);
//
//        // When
//        Product result = selfProductService.replaceProduct(productId, product);
//
//        // Then
//        assertEquals(existingProduct, result);
//        verify(productRepository, times(1)).findById(productId);
//        verify(productRepository, times(1)).save(existingProduct);
//    }
//
//    @Test
//    void replaceProduct_NotFound() {
//        // Given
//        Long productId = 1L;
//        Product product = new Product();
//        when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(NotFoundException.class, () -> selfProductService.replaceProduct(productId, product));
//        verify(productRepository, times(1)).findById(productId);
//    }
//
//    @Test
//    void deleteProduct_Success() throws NotFoundException {
//        // Given
//        Long productId = 1L;
//        Product product = new Product();
//        product.setId(productId);
//
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//
//        // When
//        Product result = selfProductService.deleteProduct(productId);
//
//        // Then
//        assertEquals(product, result);
//        verify(productRepository, times(1)).findById(productId);
//        verify(productRepository, times(1)).deleteById(productId);
//    }
//
//    @Test
//    void deleteProduct_NotFound() {
//        // Given
//        Long productId = 1L;
//        when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(NotFoundException.class, () -> selfProductService.deleteProduct(productId));
//        verify(productRepository, times(1)).findById(productId);
//    }
//}