package com.portfolio.ecommerce.config;

import com.portfolio.ecommerce.entity.Cart;
import com.portfolio.ecommerce.entity.Category;
import com.portfolio.ecommerce.entity.Product;
import com.portfolio.ecommerce.entity.Role;
import com.portfolio.ecommerce.entity.User;
import com.portfolio.ecommerce.repository.CategoryRepository;
import com.portfolio.ecommerce.repository.ProductRepository;
import com.portfolio.ecommerce.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCatalog();
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail("admin@demo.com")) {
            return;
        }

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Portfolio");
        admin.setEmail("admin@demo.com");
        admin.setPassword(passwordEncoder.encode("Admin123"));
        admin.setRoles(Set.of(Role.ADMIN, Role.USER));

        Cart cart = new Cart();
        cart.setUser(admin);
        admin.setCart(cart);

        userRepository.save(admin);
    }

    private void seedCatalog() {
        if (categoryRepository.count() > 0 || productRepository.count() > 0) {
            return;
        }

        Category keyboards = createCategory("Keyboards", "Teclados mecanicos y periféricos premium para setups modernos.");
        Category audio = createCategory("Audio", "Auriculares, parlantes y audio gear para creators y gamers.");
        Category workspace = createCategory("Workspace", "Accesorios para escritorios prolijos, productivos y visuales.");

        createProduct("Aurora 75", "Teclado mecanico compacto con switches táctiles, RGB sutil y cuerpo de aluminio.", new BigDecimal("189.90"), 12, "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?auto=format&fit=crop&w=900&q=80", keyboards);
        createProduct("Pulse Studio", "Auriculares over-ear inalámbricos con cancelación de ruido y perfil minimal.", new BigDecimal("249.00"), 20, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=900&q=80", audio);
        createProduct("Dock Slate", "Base organizadora con soporte vertical, bandeja y acabado mate premium.", new BigDecimal("79.50"), 18, "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80", workspace);
        createProduct("Mono Lamp", "Lámpara de escritorio LED con brazo articulado y temperatura regulable.", new BigDecimal("112.00"), 9, "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=900&q=80", workspace);
        createProduct("Wave One", "Speaker bluetooth compacto con sonido balanceado y diseño para escritorio.", new BigDecimal("134.99"), 14, "https://images.unsplash.com/photo-1546435770-a3e426bf472b?auto=format&fit=crop&w=900&q=80", audio);
    }

    private Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    private void createProduct(String name, String description, BigDecimal price, Integer stock, String imageUrl, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        productRepository.save(product);
    }
}
