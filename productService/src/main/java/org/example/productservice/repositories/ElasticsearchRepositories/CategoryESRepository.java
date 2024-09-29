package org.example.productservice.repositories.ElasticsearchRepositories;

import org.example.productservice.models.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryESRepository extends ElasticsearchRepository<Category, String> {
}