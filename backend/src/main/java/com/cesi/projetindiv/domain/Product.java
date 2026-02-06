package com.cesi.projetindiv.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false) private String name;
  @Column(name = "price_cents", nullable = false) private int priceCents;
  @Column(nullable = false) private int stock;

  protected Product() {}

  public Long getId() { return id; }
  public String getName() { return name; }
  public int getPriceCents() { return priceCents; }
  public int getStock() { return stock; }
  public void setStock(int stock) { this.stock = stock; }
}
