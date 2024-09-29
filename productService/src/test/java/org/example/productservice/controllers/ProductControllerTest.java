//package org.example.productservice.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.productservice.dtos.ProductResponseDto;
//import org.example.productservice.exceptions.NotFoundException;
//import org.example.productservice.models.Category;
//import org.example.productservice.models.Product;
//import org.example.productservice.services.IProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@WebMvcTest(ProductController.class)
//class ProductControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    IProductService productService;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getAllProducts_Success() throws Exception {
//        // Given
//        Product product = new Product();
//        product.setId(1L);
//        product.setTitle("Test Product");
//        product.setPrice(100.0);
//        product.setDescription("Test Description");
//        product.setImageUrl("https://example.com/image.png");
//        Category category = new Category();
//        category.setName("Technology");
//        product.setCategory(category);
//        List<Product> productList = List.of(product);
//
//        when(productService.getAllProducts()).thenReturn(productList);
//
//        ProductResponseDto responseDto = ProductResponseDto.fromProduct(product);
//        List<ProductResponseDto> responseDtosList = List.of(responseDto);
//
//        // When & Then
//        mockMvc.perform(get("/products")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(responseDtosList)));
//    }
//
//    @Test
//    void getAllProducts_NoProductsFound() throws Exception {
//        // Given
//        when(productService.getAllProducts()).thenReturn(Collections.emptyList());
//
//        // When & Then
//        mockMvc.perform(get("/products")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("{\"message\":\"No products found.\"}"));
//    }
//
//    @Test
//    void getAllProducts_Exception() throws Exception {
//        // Given
//        when(productService.getAllProducts()).thenThrow(new RuntimeException("Unexpected error"));
//
//        // When & Then
//        mockMvc.perform(get("/products")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("{\"message\":\"An unexpected error occurred: Unexpected error\"}"));
//    }
//
//
//    @Test
//    void getSingleProduct_Success() throws Exception {
//        // Given
//        Long productId = 1L;
//        Product product = new Product();
//        product.setId(productId);
//        product.setTitle("Test Product");
//        product.setPrice(100.0);
//        product.setDescription("Test Description");
//        product.setImageUrl("https://example.com/image.png");
//        Category category = new Category();
//        category.setName("Technology");
//        product.setCategory(category);
//
//        when(productService.getSingleProduct(productId)).thenReturn(Optional.of(product));
//
//        ProductResponseDto responseDto = ProductResponseDto.fromProduct(product);
//
//        // When & Then
//        mockMvc.perform(get("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
//    }
//
//    @Test
//    void getSingleProduct_NotFound() throws Exception {
//        // Given
//        Long productId = 1L;
//        when(productService.getSingleProduct(anyLong())).thenReturn(Optional.empty());
//
//        // When & Then
//        mockMvc.perform(get("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("{\"message\":\"Product with id " + productId + " not found.\"}"));
//    }
//
//    @Test
//    void getSingleProduct_Exception() throws Exception {
//        // Given
//        Long productId = 1L;
//        when(productService.getSingleProduct(anyLong())).thenThrow(new RuntimeException("Unexpected error"));
//
//        // When & Then
//        mockMvc.perform(get("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("{\"message\":\"An unexpected error occurred: Unexpected error\"}"));
//    }
//
//    @Test
//    void getAllProducts() {
//        List<Product> p = new ArrayList<>();
//        Product p1 = new Product();
//        p1.setPrice(109.95);
//        p.add(p1);
//
//        when(productService.getAllProducts()).thenReturn(p);
//
//
//    }
//
//    @Test
//    void addProduct_Success() throws Exception {
//        // Given
//        ProductResponseDto productDto = new ProductResponseDto();
//        productDto.setTitle("Test Product");
//        productDto.setPrice(100.0);
//        productDto.setDescription("Test Description");
//        productDto.setImage("https://example.com/image.png");
//        productDto.setCategory("Electronics");
//
//        Product product = productDto.toProduct();
//        Product addedProduct = new Product();
//        addedProduct.setId(1L);
//        addedProduct.setTitle(productDto.getTitle());
//        addedProduct.setPrice(productDto.getPrice());
//        addedProduct.setDescription(productDto.getDescription());
//        addedProduct.setImageUrl(productDto.getImage());
//        Category category = new Category();
//        category.setName(productDto.getCategory());
//        addedProduct.setCategory(category);
//
//        when(productService.addProduct(any(Product.class))).thenReturn(addedProduct);
//
//        ProductResponseDto responseDto = ProductResponseDto.fromProduct(addedProduct);
//
//        // When & Then
//        mockMvc.perform(post("/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productDto)))
//                .andExpect(status().isCreated())
//                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
//    }
//
//    @Test
//    void updateProduct_Success() throws Exception {
//        // Given
//        Long productId = 1L;
//        ProductResponseDto productDto = new ProductResponseDto();
//        productDto.setTitle("Updated Product");
//        productDto.setPrice(150.0);
//        productDto.setDescription("Updated Description");
//        productDto.setImage("https://example.com/image-updated.png");
//        productDto.setCategory("Updated Category");
//
//        Product product = productDto.toProduct();
//        Product updatedProduct = new Product();
//        updatedProduct.setId(productId);
//        updatedProduct.setTitle(productDto.getTitle());
//        updatedProduct.setPrice(productDto.getPrice());
//        updatedProduct.setDescription(productDto.getDescription());
//        updatedProduct.setImageUrl(productDto.getImage());
//        Category category = new Category();
//        category.setName(productDto.getCategory());
//        updatedProduct.setCategory(category);
//
//        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(updatedProduct);
//
//        ProductResponseDto responseDto = ProductResponseDto.fromProduct(updatedProduct);
//
//        // When & Then
//        mockMvc.perform(patch("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
//    }
//
//    @Test
//    void replaceProduct_Success() throws Exception {
//        // Given
//        Long productId = 1L;
//        ProductResponseDto productDto = new ProductResponseDto();
//        productDto.setTitle("Replaced Product");
//        productDto.setPrice(200.0);
//        productDto.setDescription("Replaced Description");
//        productDto.setImage("https://example.com/image-replaced.png");
//        productDto.setCategory("Replaced Category");
//
//        Product product = productDto.toProduct();
//        Product replacedProduct = new Product();
//        replacedProduct.setId(productId);
//        replacedProduct.setTitle(productDto.getTitle());
//        replacedProduct.setPrice(productDto.getPrice());
//        replacedProduct.setDescription(productDto.getDescription());
//        replacedProduct.setImageUrl(productDto.getImage());
//        Category category = new Category();
//        category.setName(productDto.getCategory());
//        replacedProduct.setCategory(category);
//
//        when(productService.replaceProduct(anyLong(), any(Product.class))).thenReturn(replacedProduct);
//
//        ProductResponseDto responseDto = ProductResponseDto.fromProduct(replacedProduct);
//
//        // When & Then
//        mockMvc.perform(put("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
//    }
//
//    @Test
//    void deleteProduct_Success() throws Exception {
//        // Given
//        Long productId = 1L;
//        Product deletedProduct = new Product();
//        deletedProduct.setId(productId);
//        deletedProduct.setTitle("Deleted Product");
//        deletedProduct.setTitle("Test Product");
//        deletedProduct.setPrice(100.0);
//        deletedProduct.setDescription("Test Description");
//        deletedProduct.setImageUrl("https://example.com/image.png");
//        Category category = new Category();
//        category.setName("Technology");
//        deletedProduct.setCategory(category);
//
//
//        when(productService.deleteProduct(anyLong())).thenReturn(deletedProduct);
//
//        ProductResponseDto responseDto = ProductResponseDto.fromProduct(deletedProduct);
//
//        // When & Then
//        mockMvc.perform(delete("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
//    }
//
//    @Test
//    void addProduct_Exception() throws Exception {
//        // Given
//        ProductResponseDto productDto = new ProductResponseDto();
//        productDto.setTitle("Test Product");
//
//        when(productService.addProduct(any(Product.class))).thenThrow(new RuntimeException("Error occured while adding products"));
//
//        // When & Then
//        mockMvc.perform(post("/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productDto)))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("{\"message\":\"An unexpected error occurred: Error occured while adding products\"}"));
//    }
//
//    @Test
//    void updateProduct_NotFoundException() throws Exception {
//        // Given
//        Long productId = 1L;
//        ProductResponseDto productDto = new ProductResponseDto();
//        productDto.setTitle("Updated Product");
//
//        when(productService.updateProduct(anyLong(), any(Product.class))).thenThrow(new NotFoundException("Product not found"));
//
//        // When & Then
//        mockMvc.perform(patch("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productDto)))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("{\"message\":\"Product not found\"}"));
//    }
//
//    @Test
//    void replaceProduct_NotFoundException() throws Exception {
//        // Given
//        Long productId = 1L;
//        ProductResponseDto productDto = new ProductResponseDto();
//        productDto.setTitle("Replaced Product");
//
//        when(productService.replaceProduct(anyLong(), any(Product.class))).thenThrow(new NotFoundException("Product not found"));
//
//        // When & Then
//        mockMvc.perform(put("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productDto)))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("{\"message\":\"Product not found\"}"));
//    }
//
//    @Test
//    void deleteProduct_NotFoundException() throws Exception {
//        // Given
//        Long productId = 1L;
//
//        when(productService.deleteProduct(anyLong())).thenThrow(new NotFoundException("Product not found"));
//
//        // When & Then
//        mockMvc.perform(delete("/products/{productId}", productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("{\"message\":\"Product not found\"}"));
//    }
//
//
//}