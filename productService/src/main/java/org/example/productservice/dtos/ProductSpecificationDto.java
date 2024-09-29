package org.example.productservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.productservice.models.Product;
import org.example.productservice.models.ProductSpecification;

@Getter
@Setter
public class ProductSpecificationDto {
    private Long id;
    private Long productId;
    private String attributeName;
    private String attributeValue;

    // Convert Entity to DTO
    public static ProductSpecificationDto fromProductSpecification(ProductSpecification specification) {
        ProductSpecificationDto dto = new ProductSpecificationDto();
        dto.setId(specification.getId());
        dto.setProductId(specification.getProduct() != null ? specification.getProduct().getId() : null);
        dto.setAttributeName(specification.getAttributeName());
        dto.setAttributeValue(specification.getAttributeValue());
        return dto;
    }

    // Convert DTO to Entity
    public ProductSpecification toProductSpecification(Product product) {
        ProductSpecification specification = new ProductSpecification();
        specification.setId(this.id);
        specification.setProduct(product);
        specification.setAttributeName(this.attributeName);
        specification.setAttributeValue(this.attributeValue);
        return specification;
    }
}

