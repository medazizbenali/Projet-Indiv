package com.cesi.projetindiv.repository;

import com.cesi.projetindiv.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserIdOrderByIdDesc(String userId);
}
