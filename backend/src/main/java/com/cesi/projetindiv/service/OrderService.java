package com.cesi.projetindiv.service;

import com.cesi.projetindiv.api.dto.*;
import com.cesi.projetindiv.domain.*;
import com.cesi.projetindiv.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
    this.productRepository = productRepository;
    this.orderRepository = orderRepository;
  }

  @Transactional
  public CreateOrderResponse createOrder(String userId, CreateOrderRequest request) {
    Order order = new Order(userId, "CREATED");
    int total = 0;

    for (CreateOrderRequest.OrderItemRequest item : request.getItems()) {
      if (item.getProductId() == null || item.getQuantity() <= 0) {
        throw new IllegalArgumentException("Invalid order item");
      }

      Product product = productRepository.findById(item.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));

      if (product.getStock() < item.getQuantity()) {
        throw new IllegalStateException("Insufficient stock for product " + product.getId());
      }

      product.setStock(product.getStock() - item.getQuantity());
      total += product.getPriceCents() * item.getQuantity();

      OrderLine line = new OrderLine(product, item.getQuantity(), product.getPriceCents());
      order.addLine(line);
    }

    order.setTotalCents(total);
    Order saved = orderRepository.save(order);
    return new CreateOrderResponse(saved.getId(), saved.getStatus(), saved.getTotalCents());
  }

  @Transactional(readOnly = true)
  public List<OrderSummaryDto> getMyOrders(String userId) {
    return orderRepository.findByUserIdOrderByIdDesc(userId).stream()
      .map(o -> new OrderSummaryDto(o.getId(), o.getCreatedAt(), o.getStatus(), o.getTotalCents()))
      .toList();
  }
}
