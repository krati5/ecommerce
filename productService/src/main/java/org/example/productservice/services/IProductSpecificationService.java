package org.example.productservice.services;

import org.example.productservice.dtos.ProductSpecificationDto;
import org.example.productservice.exceptions.NotFoundException;

import java.util.List;

public interface IProductSpecificationService {
    // Get all specifications
    List<ProductSpecificationDto> getAllSpecifications();

    // Search specifications by attribute name and value
    List<ProductSpecificationDto> searchSpecificationsByAttributeNameAndValue(String attributeName, String attributeValue) throws NotFoundException;

    ProductSpecificationDto createSpecification(ProductSpecificationDto specificationDto) throws NotFoundException;

    ProductSpecificationDto updateSpecification(Long id, ProductSpecificationDto updatedSpecDto) throws NotFoundException;

    void deleteSpecification(Long id) throws NotFoundException;

    ProductSpecificationDto getSpecificationById(Long id) throws NotFoundException;

    List<ProductSpecificationDto> getSpecificationsByProductId(Long productId);
}
