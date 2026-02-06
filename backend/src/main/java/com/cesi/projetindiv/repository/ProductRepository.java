package com.cesi.projetindiv.repository;

import com.cesi.projetindiv.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
