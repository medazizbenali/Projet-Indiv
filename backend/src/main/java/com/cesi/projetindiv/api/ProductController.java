package com.cesi.projetindiv.api;

import com.cesi.projetindiv.domain.Product;
import com.cesi.projetindiv.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductRepository productRepository;
  public ProductController(ProductRepository productRepository) { this.productRepository = productRepository; }

  @GetMapping
  public List<Product> list() { return productRepository.findAll(); }
}
