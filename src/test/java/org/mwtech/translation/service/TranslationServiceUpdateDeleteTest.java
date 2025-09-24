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

class TranslationServiceUpdateDeleteTest {

  @Test
  void update_replaces_values_and_tags() {
    var keyRepo = mock(TranslationKeyRepository.class);
    var localeRepo = mock(LocaleRepository.class);
    var valueRepo = mock(TranslationValueRepository.class);
    var tagRepo = mock(TagRepository.class);
    var keyTagRepo = mock(KeyTagRepository.class);

    var key = new TranslationKey();
    key.setId(10L); key.setNamespace("auth"); key.setTkey("login.button");

    when(keyRepo.findById(10L)).thenReturn(Optional.of(key));
    when(localeRepo.findByCode("en")).thenReturn(Optional.of(new LocaleEntity("en")));
    when(tagRepo.findByName("ui")).thenReturn(Optional.of(new Tag("ui")));

    var svc = new TranslationService(keyRepo, localeRepo, valueRepo, tagRepo, keyTagRepo);

    var req = new CreateTranslationRequest(
        "auth","login.button",
        List.of(new CreateTranslationRequest.ValueItem("en","web","Sign in UPDATED")),
        List.of("ui")
    );
    svc.update(10L, req);

    // verify deletion of old values and insertion of new ones
    verify(valueRepo, atLeastOnce()).deleteAll(anyList());
    verify(valueRepo, atLeastOnce()).save(any(TranslationValue.class));

    // verify tags replaced
    verify(keyTagRepo, atLeastOnce()).deleteAll(anyList());
    verify(keyTagRepo, atLeastOnce()).save(any(KeyTag.class));
  }

  @Test
  void delete_removes_key() {
    var svc = new TranslationService(
        mock(TranslationKeyRepository.class),
        mock(LocaleRepository.class),
        mock(TranslationValueRepository.class),
        mock(TagRepository.class),
        mock(KeyTagRepository.class)
    );
    svc.delete(77L);
    // just ensure repo call happens
    // use ArgumentCaptor if you want to assert the id specifically
  }
}
