package com.cesi.projetindiv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiFlowIT {
  @Autowired MockMvc mvc;

  @Test
  void should_list_products_publicly() throws Exception {
    mvc.perform(get("/api/products"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").exists());
  }

  @Test
  void should_create_order_and_list_history() throws Exception {
    // register (idempotent for test DB)
    String register = "{\"email\":\"admin@example.com\",\"password\":\"admin1234\"}";
    mvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(register))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token").exists());

    String login = "{\"email\":\"admin@example.com\",\"password\":\"admin1234\"}";
    String token = mvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(login))
      .andExpect(status().isOk())
      .andReturn().getResponse().getContentAsString();

    // naive extraction (small test) - token is the first value after "token":"
    String jwt = token.split("\"token\":\"")[1].split("\"")[0];

    String body = "{\"items\":[{\"productId\":1,\"quantity\":1}]}";
    mvc.perform(post("/api/orders").header("Authorization", "Bearer " + jwt)
        .contentType(MediaType.APPLICATION_JSON).content(body))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.orderId").exists());

    mvc.perform(get("/api/me/orders").header("Authorization", "Bearer " + jwt))
      .andExpect(status().isOk());
  }
}
