package org.example.productservice.controllers;

import io.micrometer.common.lang.Nullable;
import jakarta.websocket.server.PathParam;
import org.example.productservice.clients.authenticationclient.AuthenticationClient;
import org.example.productservice.clients.authenticationclient.dtos.Role;
import org.example.productservice.clients.authenticationclient.dtos.SessionStatus;
import org.example.productservice.clients.authenticationclient.dtos.ValidateTokenResponseDto;
import org.example.productservice.dtos.GetProductsRequestDto;
import org.example.productservice.dtos.ProductDto;
import org.example.productservice.dtos.ProductResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.productservice.exceptions.NotFoundException;
import org.example.productservice.models.Product;
import org.example.productservice.services.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private IProductService productService;
    private AuthenticationClient authenticationClient;

    public ProductController(IProductService productService, AuthenticationClient authenticationClient){
        this.productService = productService;
        this.authenticationClient = authenticationClient;
    }

    @PostMapping("/paginated")
    public ResponseEntity<Page<ProductResponseDto>> getProducts(@RequestBody GetProductsRequestDto requestDto){
        Page<Product> productPage = productService.getProducts(
                                                        requestDto.getNumOfResults(),
                                                            requestDto.getOffset(),
                                                        requestDto.getSortParamsList());
        Page<ProductResponseDto> productResponseDtos = productPage.map(ProductResponseDto::fromProduct);

        return new ResponseEntity<>(productResponseDtos,
                HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(@Nullable @RequestHeader("AUTH_TOKEN") String token, @Nullable @RequestHeader("USER_ID") Long userId) throws NotFoundException {
        try {
            // Fetch the products
            List<Product> productList = productService.getAllProducts();

            if(productList.isEmpty()){
                throw new NotFoundException("No products found.");
            }

            logger.info("All products: {}", productList);
            List<ProductResponseDto> responseDtosList = productList.stream()
                    .map(ProductResponseDto::fromProduct)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(responseDtosList,
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occured while adding product :{}.", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getSingleProduct(@PathVariable("productId") Long productId) throws NotFoundException{

        try {
            Optional<Product> productOptional = productService.getSingleProduct(productId);
            if(productOptional.isEmpty()){
                logger.error("Product with id {} not found.", productId);
                throw new NotFoundException("Product with id "+ productId+" not found.");
            }
            Product product = productOptional.get();
            logger.info("Get product: {}", product);
            ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(product);
            return new ResponseEntity<>(
                    productResponseDto,
                    HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error occured while adding product :{}.", e.getMessage(), e);
            throw e;
        }

    }

    @PostMapping("")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductDto productDto) throws Exception {

        try {
            Product product =  productDto.toProduct();
            Product addedProduct = productService.addProduct(product);
            logger.info("Added new product: {}", addedProduct);
            ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(addedProduct);
            return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occured while adding product :{}.", e.getMessage(), e);
            throw e;
        }
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("productId") Long productId,
                                 @RequestBody ProductDto productDto) throws NotFoundException {

        try {
            Product product = productDto.toProduct();
            Product savedProduct = productService.updateProduct(productId, product);
            logger.info("Updated product: {}", savedProduct);
            ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(savedProduct);
            return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            logger.error("Product with id {} not found.", productId);
            throw e;
        } catch (Exception e) {
            logger.error("Error occured while updating product :{}.", e.getMessage(), e);
            throw e;
        }

    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> replaceProduct(@PathVariable("productId") Long productId,
                                 @RequestBody ProductDto productDto) throws NotFoundException {

        try {
            Product product =  productDto.toProduct();
            Product savedProduct = productService.replaceProduct(productId, product);
            logger.info("Replaced product: {}", savedProduct);
            ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(savedProduct);
            return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            logger.error("Product with id {} not found.", productId);
            throw e;
        } catch (Exception e) {
            logger.error("Error occured while replacing product :{}.", e.getMessage(), e);
            throw e;
        }
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> deleteProduct(@PathVariable("productId") Long productId) throws NotFoundException {
        try {
            Product deletedProduct = productService.deleteProduct(productId);
            logger.info("Deleted product: {}", deletedProduct);
            ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(deletedProduct);
            return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            logger.error("Product with id {} not found.", productId);
            throw e;
        } catch (Exception e) {
            logger.error("Error occured while deleting product :{}.", e.getMessage(), e);
            throw e;
        }


    }

    @GetMapping("/search/{keyword}")
    public Iterable<Product> searchProducts(@PathVariable String keyword) {
        return productService.searchProducts(keyword);
    }


}
