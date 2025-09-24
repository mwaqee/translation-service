package org.mwtech.translation.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mwtech.translation.TranslationServiceApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TranslationServiceApplication.class)
@AutoConfigureMockMvc
class TranslationFlowTest {

  @Autowired MockMvc mvc;
  String token;

  @BeforeEach
  void login() throws Exception {
    String body = "{\"email\":\"admin@example.com\",\"password\":\"secret\"}";
    var res = mvc.perform(post("/api/v1/auth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    token = res.replaceAll(".*\\\"token\\\"\\s*:\\s*\\\"([^\\\"]+)\\\".*", "$1");
  }

  @Test
  void end_to_end_create_search_export() throws Exception {
    var payload = """
      {"namespace":"auth","tkey":"login.button",
       "values":[{"locale":"en","platform":"web","text":"Sign in"}],
       "tags":["auth","ui"]}
      """;

    // create
    mvc.perform(post("/api/v1/translations")
            .header("Authorization","Bearer "+token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload))
        .andExpect(status().isOk());

    // search
    mvc.perform(get("/api/v1/translations")
            .header("Authorization","Bearer "+token)
            .param("q","login"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", not(empty())));

    // export
    mvc.perform(get("/api/v1/export")
            .header("Authorization","Bearer "+token)
            .param("locale","en").param("platform","web"))
        .andExpect(status().isOk())
        .andExpect(header().exists("ETag"))
        .andExpect(jsonPath("$.['login.button']", is("Sign in")));
  }

  @Test
  void unauthorized_requests_are_rejected() throws Exception {
    mvc.perform(get("/api/v1/translations"))
        .andExpect(status().isUnauthorized());
  }
}
