package org.example.productservice.repositories.JpaRepositories;


import org.example.productservice.models.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    List<ProductSpecification> findByProductId(Long productId);

    // Query to search by attribute name and value
    List<ProductSpecification> findByAttributeNameAndAttributeValue(String attributeName, String attributeValue);
}