package com.cesi.projetindiv.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_lines")
public class OrderLine {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false) @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(optional = false) @JoinColumn(name = "product_id")
  private Product product;

  @Column(nullable = false) private int quantity;
  @Column(name = "unit_price_cents", nullable = false) private int unitPriceCents;

  protected OrderLine() {}

  public OrderLine(Product product, int quantity, int unitPriceCents) {
    this.product = product; this.quantity = quantity; this.unitPriceCents = unitPriceCents;
  }

  void setOrder(Order order) { this.order = order; }

  public Long getId() { return id; }
  public Product getProduct() { return product; }
  public int getQuantity() { return quantity; }
  public int getUnitPriceCents() { return unitPriceCents; }
}
