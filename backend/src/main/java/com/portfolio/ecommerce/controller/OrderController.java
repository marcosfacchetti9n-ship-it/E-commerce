package com.portfolio.ecommerce.controller;

import com.portfolio.ecommerce.dto.order.OrderResponse;
import com.portfolio.ecommerce.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public OrderResponse checkout(@AuthenticationPrincipal(expression = "username") String email) {
        return orderService.checkout(email);
    }

    @GetMapping("/me")
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal(expression = "username") String email) {
        return orderService.getMyOrders(email);
    }
}
