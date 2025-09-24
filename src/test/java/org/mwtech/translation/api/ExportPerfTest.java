package org.mwtech.translation.api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mwtech.translation.TranslationServiceApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TranslationServiceApplication.class)
@AutoConfigureMockMvc
class ExportPerfTest {

  @Autowired MockMvc mvc;

  @Test
  @Disabled("Enable locally after seeding 100k+ to measure. CI timing is noisy.")
  void export_is_fast_enough_under_load() throws Exception {
    long t0 = System.currentTimeMillis();
    mvc.perform(get("/api/v1/export").param("locale","en").param("platform","web"))
        .andExpect(status().isOk());
    long elapsed = System.currentTimeMillis() - t0;
    assert elapsed < 500 : "Export took " + elapsed + "ms";
  }
}
