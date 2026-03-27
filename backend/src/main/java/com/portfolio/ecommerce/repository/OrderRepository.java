package com.portfolio.ecommerce.repository;

import com.portfolio.ecommerce.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByUserEmailOrderByCreatedAtDesc(String email);
}
