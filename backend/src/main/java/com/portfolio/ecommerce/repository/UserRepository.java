package com.portfolio.ecommerce.repository;

import com.portfolio.ecommerce.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"cart", "cart.items", "cart.items.product"})
    Optional<User> findWithCartByEmail(String email);
}
