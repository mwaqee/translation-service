package org.mwtech.translation.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ExportServiceTest {

  @Test
  void returns_304_when_etag_matches() {
    TranslationService svc = Mockito.mock(TranslationService.class);
    when(svc.lastUpdated()).thenReturn(Instant.ofEpochMilli(123456789L));
    when(svc.export("en","web", null)).thenReturn(Map.of("a","b"));

    ExportService es = new ExportService(svc);
    var first = es.export("en","web", null, null, null);
    String etag = first.getHeaders().getETag();

    var second = es.export("en","web", null, etag, null);
    assertThat(second.getStatusCode().value()).isEqualTo(304);
  }
}
