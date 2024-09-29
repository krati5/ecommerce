package org.example.productservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.AllArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.example.productservice.clients.fakestoreapi.FakeStoreProductDto;
import org.example.productservice.dtos.ProductDto;
import org.example.productservice.dtos.ProductResponseDto;
import org.example.productservice.exceptions.NotFoundException;
import org.example.productservice.models.Category;
import org.example.productservice.models.Product;
import org.example.productservice.models.SortParam;
import org.example.productservice.models.SortType;
import org.example.productservice.repositories.ElasticsearchRepositories.ProductESRepository;
import org.example.productservice.repositories.JpaRepositories.CategoryRepository;
import org.example.productservice.repositories.JpaRepositories.ProductRepository;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Primary
public class SelfProductService implements IProductService{
    private static final Logger logger = LoggerFactory.getLogger(SelfProductService.class);
    private ProductRepository productRepository ;
    private CategoryRepository categoryRepository ;

    private final ProductESRepository productESRepository;
    private RedisTemplate<String, Object> redisTemplate;

    private Category getOrSetCategory(Category category){
        // Ensure the category is saved or retrieved from the repository
        if (category != null) {
            Category savedCategory = categoryRepository.findByName(category.getName())
                    .orElseGet(() -> categoryRepository.save(category));
            return savedCategory;

        }
        return null;
    }


    @Override
    public Page<Product> getProducts(int numOfResults, int offset, List<SortParam> sortParams){
        Sort sort ;
        if(sortParams.getFirst().getSortType().equals(SortType.ASC)){
            sort = Sort.by(sortParams.getFirst().getParamName()).ascending();
        }
        else{
            sort = Sort.by(sortParams.getFirst().getParamName()).descending();
        }

        for(int i=1; i<sortParams.size(); i++){
            if(sortParams.get(i).getSortType().equals(SortType.ASC)){
                sort.and(Sort.by(sortParams.get(i).getParamName()).ascending());
            }
            else{
                sort = sort.and(Sort.by(sortParams.get(i).getParamName()).descending());

            }
        }


        return productRepository.findAll(
                // page number starts from 0
                PageRequest.of((offset/numOfResults), numOfResults, sort)
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getSingleProduct(Long productId) {
        ProductResponseDto responseDto = (ProductResponseDto)redisTemplate.opsForHash().get("PRODUCTS", productId);
        if(responseDto!=null){
            return Optional.of(responseDto.toProduct());
        }
        // Query the repository fetch the product
        Optional<Product> productOptional = productRepository.findById(productId);

        if(productOptional.isPresent()){
            redisTemplate.opsForHash().put("PRODUCTS", productId, ProductResponseDto.fromProduct(productOptional.get()));
        }

        return productOptional;

    }

    @Override
    public Product addProduct(Product product) {
        product.setCategory(getOrSetCategory(product.getCategory()));
        // first save data in SQL DB and then in elastic search
        Product newProduct = productRepository.save(product);
//        productESRepository.save(product);
        ESUtils.mapForES(Product.class, newProduct).map(res -> productESRepository.save(res));
        return newProduct;
    }



    @Override
    public Product updateProduct(Long productId, Product product) throws NotFoundException {
        product.setCategory(getOrSetCategory(product.getCategory()));

        Optional<Product> existingProductOptional = getSingleProduct(productId);
        if(existingProductOptional.isEmpty()){
            throw new NotFoundException("Product with id "+ productId+" not found.");
        }

        Product existingProduct = existingProductOptional.get();
        // Update only the fields that are present in the updatedProduct
        if (product.getId() != null) {
            existingProduct.setId(productId);
            }
        if (product.getImageUrl() != null) {
            existingProduct.setImageUrl(product.getImageUrl());
        }
        if (product.getTitle() != null) {
            existingProduct.setTitle(product.getTitle());
        }
        if (product.getDescription() != null) {
            existingProduct.setDescription(product.getDescription());
        }
        if (product.getPrice() != null) {
            existingProduct.setPrice(product.getPrice());
        }
        if (product.getCategory() != null) {
            existingProduct.setCategory(product.getCategory());
        }
        if(product.getStockQuantity() != null){
            existingProduct.setStockQuantity(product.getStockQuantity());
        }
        // Save updated product
        productESRepository.save(existingProduct); // Update in Elasticsearch
        return productRepository.save(existingProduct);

    }

    @Override
    public Product replaceProduct(Long productId, Product product) throws NotFoundException {
        product.setCategory(getOrSetCategory(product.getCategory()));

        Optional<Product> existingProductOptional = getSingleProduct(productId);
        if(existingProductOptional.isEmpty()){
            throw new NotFoundException("Product with id "+ productId+" not found.");
        }

        Product existingProduct = existingProductOptional.get();
        // Update fields
        existingProduct.setId(productId);
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStockQuantity(product.getStockQuantity());

        // Save updated product
//        productESRepository.save(existingProduct); // Save to Elasticsearch
        return productRepository.save(existingProduct);

    }

    @Override
    public Product deleteProduct(Long productId) throws NotFoundException {
        Optional<Product> existingProductOptional = getSingleProduct(productId);
        if(existingProductOptional.isEmpty()){
            throw new NotFoundException("Product with id "+ productId+" not found.");
        }

        productRepository.deleteById(productId);
        productESRepository.deleteById(productId); // Remove from Elasticsearch
        return existingProductOptional.get();
    }


    @Override
    public List<Product> searchProducts(String keyword) {
        return productESRepository.findAllByTitleContaining(keyword);

    }
}
