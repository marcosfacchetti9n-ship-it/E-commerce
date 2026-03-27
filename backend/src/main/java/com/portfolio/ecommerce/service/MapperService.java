package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.auth.AuthResponse;
import com.portfolio.ecommerce.dto.cart.CartItemResponse;
import com.portfolio.ecommerce.dto.cart.CartResponse;
import com.portfolio.ecommerce.dto.category.CategoryResponse;
import com.portfolio.ecommerce.dto.order.OrderItemResponse;
import com.portfolio.ecommerce.dto.order.OrderResponse;
import com.portfolio.ecommerce.dto.product.ProductResponse;
import com.portfolio.ecommerce.entity.Cart;
import com.portfolio.ecommerce.entity.CartItem;
import com.portfolio.ecommerce.entity.Category;
import com.portfolio.ecommerce.entity.Order;
import com.portfolio.ecommerce.entity.OrderItem;
import com.portfolio.ecommerce.entity.Product;
import com.portfolio.ecommerce.entity.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MapperService {

    public AuthResponse toAuthResponse(String token, User user) {
        return new AuthResponse(
                token,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(Enum::name).collect(Collectors.toSet())
        );
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription());
    }

    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                toCategoryResponse(product.getCategory())
        );
    }

    public CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toCartItemResponse)
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), items, total);
    }

    public CartItemResponse toCartItemResponse(CartItem item) {
        BigDecimal subtotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getImageUrl(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                subtotal
        );
    }

    public OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotal(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getItems().stream().map(this::toOrderItemResponse).toList()
        );
    }

    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct().getId(),
                item.getProductName(),
                item.getProduct().getImageUrl(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
