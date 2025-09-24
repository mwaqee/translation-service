package org.mwtech.translation.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mwtech.translation.TranslationServiceApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TranslationServiceApplication.class)
@AutoConfigureMockMvc
class ExportController304Test {

  @Autowired MockMvc mvc;
  String token;

  @BeforeEach
  void login() throws Exception {
    String body = "{\"email\":\"admin@example.com\",\"password\":\"secret\"}";
    var res = mvc.perform(post("/api/v1/auth/token")
            .contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    token = res.replaceAll(".*\\\"token\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");
  }

  @Test
  void export_returns_304_when_etag_matches() throws Exception {
    // First call: 200 with ETag
    var resp = mvc.perform(get("/api/v1/export")
            .header("Authorization","Bearer "+token)
            .param("locale","en").param("platform","web"))
        .andExpect(status().isOk())
        .andExpect(header().exists("ETag"))
        .andReturn().getResponse();

    String etag = resp.getHeader("ETag");

    // Second call with If-None-Match: expect 304
    mvc.perform(get("/api/v1/export")
            .header("Authorization","Bearer "+token)
            .header("If-None-Match", etag)
            .param("locale","en").param("platform","web"))
        .andExpect(status().isNotModified());
  }
}
