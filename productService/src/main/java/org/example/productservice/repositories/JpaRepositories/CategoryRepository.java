package org.example.productservice.repositories.JpaRepositories;

import org.example.productservice.models.Category;
import org.example.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String categoryName);

//    List<Product> findProductsByCategory(Category category);
}
