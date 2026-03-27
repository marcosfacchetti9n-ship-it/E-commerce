package com.portfolio.ecommerce.repository;

import com.portfolio.ecommerce.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = "category")
    List<Product> findAll();

    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Long categoryId);

    @Override
    @EntityGraph(attributePaths = "category")
    Optional<Product> findById(Long id);
}
