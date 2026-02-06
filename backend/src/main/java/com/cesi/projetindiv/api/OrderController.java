package com.cesi.projetindiv.api;

import com.cesi.projetindiv.api.dto.*;
import com.cesi.projetindiv.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/api/orders")
  public ResponseEntity<CreateOrderResponse> create(@Valid @RequestBody CreateOrderRequest request,
                                                    Authentication auth) {
    String userId = auth.getName();
    return ResponseEntity.ok(orderService.createOrder(userId, request));
  }

  @GetMapping("/api/me/orders")
  public List<OrderSummaryDto> myOrders(Authentication auth) {
    return orderService.getMyOrders(auth.getName());
  }

  @GetMapping("/api/me/orders/{id}")
  public OrderDetailDto myOrder(@PathVariable Long id, Authentication auth) {
    return orderService.getMyOrder(auth.getName(), id);
  }
}
