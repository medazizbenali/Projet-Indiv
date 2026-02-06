package com.cesi.projetindiv.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class OrderDetailDto {
  private Long id;
  private OffsetDateTime createdAt;
  private String status;
  private int totalCents;
  private List<OrderLineDto> lines;

  public OrderDetailDto(Long id, OffsetDateTime createdAt, String status, int totalCents, List<OrderLineDto> lines) {
    this.id = id;
    this.createdAt = createdAt;
    this.status = status;
    this.totalCents = totalCents;
    this.lines = lines;
  }

  public Long getId() { return id; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public String getStatus() { return status; }
  public int getTotalCents() { return totalCents; }
  public List<OrderLineDto> getLines() { return lines; }
}
