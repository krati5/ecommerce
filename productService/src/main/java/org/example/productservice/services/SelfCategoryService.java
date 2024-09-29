package org.example.productservice.services;

import lombok.AllArgsConstructor;
import org.example.productservice.dtos.CategoryRequestDto;
import org.example.productservice.exceptions.NotFoundException;
import org.example.productservice.models.Category;
import org.example.productservice.models.Product;
import org.example.productservice.repositories.JpaRepositories.CategoryRepository;
import org.example.productservice.repositories.JpaRepositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@AllArgsConstructor
public class SelfCategoryService implements ICategoryService{
    private static final Logger logger = LoggerFactory.getLogger(SelfProductService.class);
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(CategoryRequestDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryRequestDto categoryDto) throws NotFoundException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(optionalCategory.isEmpty()){
            throw new NotFoundException("Category with id "+id+" not found.");
        }

        Category category = optionalCategory.get();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return categoryRepository.save(category);

    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsInCategory(String categoryName) throws NotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        if(categoryOptional.isEmpty()){
            throw new NotFoundException("Category with name "+categoryName+" not found.");
        }
        Category category = categoryOptional.get();
        return category.getProducts();
    }

}
