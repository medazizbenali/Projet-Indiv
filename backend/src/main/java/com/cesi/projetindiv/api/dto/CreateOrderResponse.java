package com.cesi.projetindiv.api.dto;

public class CreateOrderResponse {
  private Long orderId;
  private String status;
  private int totalCents;

  public CreateOrderResponse(Long orderId, String status, int totalCents) {
    this.orderId = orderId;
    this.status = status;
    this.totalCents = totalCents;
  }

  public Long getOrderId() { return orderId; }
  public String getStatus() { return status; }
  public int getTotalCents() { return totalCents; }
}
