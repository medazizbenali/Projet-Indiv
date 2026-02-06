package com.cesi.projetindiv.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(nullable = false)
  private String status;

  @Column(name = "total_cents", nullable = false)
  private int totalCents;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderLine> lines = new ArrayList<>();

  protected Order() {}

  public Order(String userId, String status) { this.userId = userId; this.status = status; }

  public void addLine(OrderLine line) { lines.add(line); line.setOrder(this); }

  public Long getId() { return id; }
  public String getUserId() { return userId; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public String getStatus() { return status; }
  public int getTotalCents() { return totalCents; }
  public List<OrderLine> getLines() { return lines; }

  public void setTotalCents(int totalCents) { this.totalCents = totalCents; }
}
