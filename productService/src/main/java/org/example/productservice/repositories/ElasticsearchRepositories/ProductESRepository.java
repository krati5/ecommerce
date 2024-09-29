package org.example.productservice.repositories.ElasticsearchRepositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.example.productservice.models.Product;

import java.util.List;

@Repository
public interface ProductESRepository extends ElasticsearchRepository<Product, Long> {
    List<Product> findAllByTitleContaining(String query);


    // Example with custom Query
//    @Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
//    Page<Article> findByAuthorsNameUsingCustomQuery(String name, Pageable pageable);
}
