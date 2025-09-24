package org.mwtech.translation.service;

import org.junit.jupiter.api.Test;
import org.mwtech.translation.domain.LocaleEntity;
import org.mwtech.translation.domain.Tag;
import org.mwtech.translation.domain.TranslationKey;
import org.mwtech.translation.domain.TranslationValue;
import org.mwtech.translation.repo.*;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class TranslationServiceExportMapTest {

  @Test
  void export_builds_flat_map_from_repo_rows() {
    var keyRepo = Mockito.mock(TranslationKeyRepository.class);
    var localeRepo = Mockito.mock(LocaleRepository.class);
    var valueRepo = Mockito.mock(TranslationValueRepository.class);
    var tagRepo = Mockito.mock(TagRepository.class);
    var keyTagRepo = Mockito.mock(KeyTagRepository.class);

    // Simulate repository returning rows: [tkey, text]
    when(valueRepo.exportRows("en", "web", null))
        .thenReturn(List.of(new Object[]{"login.button", "Sign in"},
                            new Object[]{"home.title", "Welcome"}));

    var svc = new TranslationService(keyRepo, localeRepo, valueRepo, tagRepo, keyTagRepo);
    Map<String,String> map = svc.export("en", "web", null);

    assertThat(map).containsEntry("login.button", "Sign in");
    assertThat(map).containsEntry("home.title", "Welcome");
  }

  @Test
  void lastUpdated_defaults_to_epoch_when_null() {
    var keyRepo = Mockito.mock(TranslationKeyRepository.class);
    var localeRepo = Mockito.mock(LocaleRepository.class);
    var valueRepo = Mockito.mock(TranslationValueRepository.class);
    var tagRepo = Mockito.mock(TagRepository.class);
    var keyTagRepo = Mockito.mock(KeyTagRepository.class);

    when(valueRepo.maxUpdated()).thenReturn(null);

    var svc = new TranslationService(keyRepo, localeRepo, valueRepo, tagRepo, keyTagRepo);
    Instant t = svc.lastUpdated();
    assertThat(t).isEqualTo(Instant.EPOCH);
  }
}
