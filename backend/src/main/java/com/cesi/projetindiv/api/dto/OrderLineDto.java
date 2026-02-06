package com.cesi.projetindiv.api.dto;

public class OrderLineDto {
  private Long productId;
  private String productName;
  private int quantity;
  private int unitPriceCents;

  public OrderLineDto(Long productId, String productName, int quantity, int unitPriceCents) {
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.unitPriceCents = unitPriceCents;
  }

  public Long getProductId() { return productId; }
  public String getProductName() { return productName; }
  public int getQuantity() { return quantity; }
  public int getUnitPriceCents() { return unitPriceCents; }
}
