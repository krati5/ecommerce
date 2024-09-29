package org.example.productservice.controllers;


import org.example.productservice.dtos.ProductSpecificationDto;
import org.example.productservice.exceptions.NotFoundException;
import org.example.productservice.models.ProductSpecification;
import org.example.productservice.services.ProductSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specifications")
public class ProductSpecificationController {
    @Autowired
    private ProductSpecificationService specificationService;

    @PostMapping
    public ResponseEntity<ProductSpecificationDto> createSpecification(@RequestBody ProductSpecificationDto specificationDto) throws NotFoundException {
        ProductSpecificationDto createdSpecDto = specificationService.createSpecification(specificationDto);
        return ResponseEntity.ok(createdSpecDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductSpecificationDto> updateSpecification(@PathVariable Long id, @RequestBody ProductSpecificationDto specificationDto) throws NotFoundException {
        ProductSpecificationDto updatedSpecDto = specificationService.updateSpecification(id, specificationDto);
        return ResponseEntity.ok(updatedSpecDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable Long id) throws NotFoundException {
        specificationService.deleteSpecification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSpecificationDto> getSpecificationById(@PathVariable Long id) throws NotFoundException {
        ProductSpecificationDto specificationDto = specificationService.getSpecificationById(id);
        return ResponseEntity.ok(specificationDto);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductSpecificationDto>> getSpecificationsByProductId(@PathVariable Long productId) {
        List<ProductSpecificationDto> specificationsDto = specificationService.getSpecificationsByProductId(productId);
        return ResponseEntity.ok(specificationsDto);
    }

    // Get all specifications
    @GetMapping
    public ResponseEntity<List<ProductSpecificationDto>> getAllSpecifications() {
        List<ProductSpecificationDto> specifications = specificationService.getAllSpecifications();
        return new ResponseEntity<>(specifications, HttpStatus.OK);
    }

    // Search specifications by attribute name and value
    @GetMapping("/{attributeName}/{attributeValue}")
    public ResponseEntity<List<ProductSpecificationDto>> searchSpecificationsByAttributeNameAndValue(
            @PathVariable String attributeName,
            @PathVariable String attributeValue) throws NotFoundException {
        List<ProductSpecificationDto> specifications = specificationService.searchSpecificationsByAttributeNameAndValue(attributeName, attributeValue);
        return new ResponseEntity<>(specifications, HttpStatus.OK);
    }
}