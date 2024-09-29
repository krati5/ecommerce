package org.example.productservice.repositories.ElasticsearchRepositories;

import org.example.productservice.models.ProductSpecification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSpecificationESRepository extends ElasticsearchRepository<ProductSpecification, String> {
}