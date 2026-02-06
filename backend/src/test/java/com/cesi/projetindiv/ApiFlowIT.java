package com.cesi.projetindiv;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
    String body = "{\"items\":[{\"productId\":1,\"quantity\":2},{\"productId\":2,\"quantity\":1}]}";

    mvc.perform(post("/api/orders")
        .with(httpBasic("aziz","aziz123"))
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.orderId").exists())
      .andExpect(jsonPath("$.totalCents").exists());

    mvc.perform(get("/api/me/orders").with(httpBasic("aziz","aziz123")))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").exists());
  }
}
