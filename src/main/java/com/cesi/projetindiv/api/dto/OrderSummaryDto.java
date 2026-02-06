package com.cesi.projetindiv.api.dto;

import java.time.OffsetDateTime;

public class OrderSummaryDto {
  private Long id;
  private OffsetDateTime createdAt;
  private String status;
  private int totalCents;

  public OrderSummaryDto(Long id, OffsetDateTime createdAt, String status, int totalCents) {
    this.id = id;
    this.createdAt = createdAt;
    this.status = status;
    this.totalCents = totalCents;
  }

  public Long getId() { return id; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public String getStatus() { return status; }
  public int getTotalCents() { return totalCents; }
}
