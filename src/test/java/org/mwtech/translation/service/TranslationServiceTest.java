package org.mwtech.translation.service;

import org.junit.jupiter.api.Test;
import org.mwtech.translation.api.dto.CreateTranslationRequest;
import org.mwtech.translation.domain.*;
import org.mwtech.translation.repo.*;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TranslationServiceTest {

  @Test
  void create_inserts_key_values_and_tags() {
    var keyRepo = mock(TranslationKeyRepository.class);
    var localeRepo = mock(LocaleRepository.class);
    var valueRepo = mock(TranslationValueRepository.class);
    var tagRepo = mock(TagRepository.class);
    var keyTagRepo = mock(KeyTagRepository.class);

    when(keyRepo.findByNamespaceAndTkey(any(), any())).thenReturn(Optional.empty());
    when(localeRepo.findByCode("en")).thenReturn(Optional.of(new LocaleEntity("en")));
    when(tagRepo.findByName("auth")).thenReturn(Optional.of(new Tag("auth")));
    when(keyRepo.save(any())).thenAnswer(a -> { TranslationKey k = a.getArgument(0); return k; });

    var svc = new TranslationService(keyRepo, localeRepo, valueRepo, tagRepo, keyTagRepo);

    var req = new CreateTranslationRequest(
        "auth", "login.button",
        List.of(new CreateTranslationRequest.ValueItem("en","web","Sign in")),
        List.of("auth")
    );
    Long id = svc.create(req);

    verify(keyRepo, atLeastOnce()).save(any(TranslationKey.class));
    verify(valueRepo, atLeastOnce()).save(any(TranslationValue.class));
    verify(keyTagRepo, atLeastOnce()).save(any(KeyTag.class));
  }
}
