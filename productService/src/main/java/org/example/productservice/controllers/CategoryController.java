package org.example.productservice.controllers;

import org.example.productservice.dtos.CategoryDto;
import org.example.productservice.dtos.CategoryRequestDto;
import org.example.productservice.dtos.ProductResponseDto;
import org.example.productservice.exceptions.NotFoundException;
import org.example.productservice.models.Category;
import org.example.productservice.models.Product;
import org.example.productservice.services.ICategoryService;
import org.example.productservice.services.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    ICategoryService categoryService;


    public CategoryController(ICategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getAllCategories() throws NotFoundException {

        try {
            List<Category> categoryList = categoryService.getAllCategories();

            if(categoryList.isEmpty()){
                throw new NotFoundException("No categories found.");
            }

            logger.info("All categories: {}", categoryList);
            List<CategoryDto> categoryDtoList = categoryList.stream()
                    .map(CategoryDto::fromCategory)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(categoryDtoList,
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occured while adding category :{}.", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) throws NotFoundException {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        return ResponseEntity.ok(CategoryDto.fromCategory(category));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryRequestDto categoryDto) {
        try {
            Category createdCategory = categoryService.createCategory(categoryDto);
            return new ResponseEntity<>(CategoryDto.fromCategory(createdCategory), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occured while getting products in category :{}.", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto categoryDto) throws NotFoundException {
        Category updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(CategoryDto.fromCategory(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/{categoryName}/products")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable("categoryName") String categoryName) throws NotFoundException {
        try {
            List<Product> products = categoryService.getProductsInCategory(categoryName);
            List<ProductResponseDto> responseDtos = products.stream().map(ProductResponseDto::fromProduct).collect(Collectors.toList());
            return new ResponseEntity<>(responseDtos, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occured while getting products in category :{}.", e.getMessage(), e);
            throw e;
        }
    }



}
