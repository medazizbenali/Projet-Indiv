package com.cesi.projetindiv.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class CreateOrderRequest {
  @NotEmpty @Valid
  private List<OrderItemRequest> items;

  public List<OrderItemRequest> getItems() { return items; }
  public void setItems(List<OrderItemRequest> items) { this.items = items; }

  public static class OrderItemRequest {
    private Long productId;
    private int quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
  }
}
