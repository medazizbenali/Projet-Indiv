package com.cesi.projetindiv;

import com.cesi.projetindiv.api.dto.CreateOrderRequest;
import com.cesi.projetindiv.api.dto.CreateOrderResponse;
import com.cesi.projetindiv.domain.Order;
import com.cesi.projetindiv.domain.Product;
import com.cesi.projetindiv.repository.OrderRepository;
import com.cesi.projetindiv.repository.ProductRepository;
import com.cesi.projetindiv.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

  private static Product product(long id, int priceCents, int stock) throws Exception {
    Constructor<Product> c = Product.class.getDeclaredConstructor();
    c.setAccessible(true);
    Product p = c.newInstance();

    // On set les champs privés via reflection (classique en tests pour entités JPA)
    ReflectionTestUtils.setField(p, "id", id);
    ReflectionTestUtils.setField(p, "name", "Test product");
    ReflectionTestUtils.setField(p, "priceCents", priceCents);
    ReflectionTestUtils.setField(p, "stock", stock);
    return p;
  }

  @Test
  void createOrder_shouldComputeTotal_andDecreaseStock() throws Exception {
    ProductRepository productRepository = mock(ProductRepository.class);
    OrderRepository orderRepository = mock(OrderRepository.class);
    OrderService service = new OrderService(productRepository, orderRepository);

    Product p = product(10L, 500, 10); // 5€ et stock 10
    when(productRepository.findById(10L)).thenReturn(Optional.of(p));

    // Simule la génération d'ID côté DB
    when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
      Order o = inv.getArgument(0);
      ReflectionTestUtils.setField(o, "id", 99L);
      return o;
    });

    CreateOrderRequest req = new CreateOrderRequest();
    CreateOrderRequest.OrderItemRequest item = new CreateOrderRequest.OrderItemRequest();
    item.setProductId(10L);
    item.setQuantity(2);
    req.setItems(List.of(item));

    CreateOrderResponse res = service.createOrder("user-1", req);

    assertEquals(99L, res.getOrderId());
    assertEquals("CREATED", res.getStatus());
    assertEquals(1000, res.getTotalCents()); // 500 * 2
    assertEquals(8, p.getStock()); // 10 - 2

    // Vérifie que l'ordre sauvé contient bien une ligne
    ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
    verify(orderRepository).save(captor.capture());
    assertEquals(1, captor.getValue().getLines().size());
  }

  @Test
  void createOrder_shouldFail_whenInsufficientStock() throws Exception {
    ProductRepository productRepository = mock(ProductRepository.class);
    OrderRepository orderRepository = mock(OrderRepository.class);
    OrderService service = new OrderService(productRepository, orderRepository);

    Product p = product(10L, 500, 1);
    when(productRepository.findById(10L)).thenReturn(Optional.of(p));

    CreateOrderRequest req = new CreateOrderRequest();
    CreateOrderRequest.OrderItemRequest item = new CreateOrderRequest.OrderItemRequest();
    item.setProductId(10L);
    item.setQuantity(2);
    req.setItems(List.of(item));

    assertThrows(IllegalStateException.class, () -> service.createOrder("user-1", req));
    verify(orderRepository, never()).save(any());
  }
}