package com.cesi.projetindiv.api.dto;

public record AuthResponse(
  String token,
  String tokenType,
  long expiresInSeconds
) {}
