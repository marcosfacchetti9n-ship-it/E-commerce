package com.portfolio.ecommerce.service;

import com.portfolio.ecommerce.dto.order.OrderResponse;
import com.portfolio.ecommerce.entity.Cart;
import com.portfolio.ecommerce.entity.CartItem;
import com.portfolio.ecommerce.entity.Order;
import com.portfolio.ecommerce.entity.OrderItem;
import com.portfolio.ecommerce.entity.OrderStatus;
import com.portfolio.ecommerce.entity.Product;
import com.portfolio.ecommerce.entity.User;
import com.portfolio.ecommerce.exception.BadRequestException;
import com.portfolio.ecommerce.exception.NotFoundException;
import com.portfolio.ecommerce.repository.OrderRepository;
import com.portfolio.ecommerce.repository.ProductRepository;
import com.portfolio.ecommerce.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final MapperService mapperService;

    @Transactional
    public OrderResponse checkout(String email) {
        User user = userRepository.findWithCartByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));
        Cart cart = cartService.getCartEntity(email);

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("El carrito esta vacio.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Stock insuficiente para " + product.getName() + ".");
            }
            product.setStock(product.getStock() - cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            order.getItems().add(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotal(total);
        productRepository.saveAll(order.getItems().stream().map(OrderItem::getProduct).toList());
        Order savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        return mapperService.toOrderResponse(savedOrder);
    }

    public List<OrderResponse> getMyOrders(String email) {
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email).stream()
                .map(mapperService::toOrderResponse)
                .toList();
    }
}
