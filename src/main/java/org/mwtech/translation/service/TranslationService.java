package org.mwtech.translation.service;

import org.mwtech.translation.api.dto.CreateTranslationRequest;
import org.mwtech.translation.domain.*;
import org.mwtech.translation.repo.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class TranslationService {
  private final TranslationKeyRepository keyRepo;
  private final LocaleRepository localeRepo;
  private final TranslationValueRepository valueRepo;
  private final TagRepository tagRepo;
  private final KeyTagRepository keyTagRepo;

  public TranslationService(TranslationKeyRepository keyRepo, LocaleRepository localeRepo,
                            TranslationValueRepository valueRepo, TagRepository tagRepo,
                            KeyTagRepository keyTagRepo) {
    this.keyRepo = keyRepo; this.localeRepo = localeRepo;
    this.valueRepo = valueRepo; this.tagRepo = tagRepo; this.keyTagRepo = keyTagRepo;
  }

  @Transactional
  public Long create(CreateTranslationRequest req){
    TranslationKey key = keyRepo.findByNamespaceAndTkey(req.namespace(), req.tkey())
      .orElseGet(() -> {
        TranslationKey k = new TranslationKey();
        k.setNamespace(req.namespace());
        k.setTkey(req.tkey());
        return keyRepo.save(k);
      });

    for (var v: req.values()){
      LocaleEntity loc = localeRepo.findByCode(v.locale())
        .orElseGet(() -> localeRepo.save(new LocaleEntity(v.locale())));
      TranslationValue tv = new TranslationValue();
      tv.setKey(key); tv.setLocale(loc); tv.setPlatform(v.platform()); tv.setText(v.text());
      valueRepo.save(tv);
    }

    if (req.tags()!=null){
      for (String name: req.tags()){
        Tag t = tagRepo.findByName(name).orElseGet(() -> tagRepo.save(new Tag(name)));
        keyTagRepo.save(new KeyTag(key.getId(), t.getId()));
      }
    }
    return key.getId();
  }

  @Transactional(readOnly = true)
  public Page<TranslationValue> search(String namespace, String locale, String platform, String q, Pageable pageable){
    return valueRepo.search(namespace, locale, platform, q, pageable);
  }

  @Transactional(readOnly = true)
  public Instant lastUpdated(){ return Optional.ofNullable(valueRepo.maxUpdated()).orElse(Instant.EPOCH); }

  @Transactional(readOnly = true)
  public Map<String,String> export(String locale, String platform, String namespace){
    Map<String,String> map = new LinkedHashMap<>();
    for (Object[] row: valueRepo.exportRows(locale, platform, namespace)){
      map.put((String)row[0], (String)row[1]);
    }
    return map;
  }
  
  @Transactional
  public void update(Long id, CreateTranslationRequest req) {
      TranslationKey key = keyRepo.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("Key not found: " + id));

      key.setNamespace(req.namespace());
      key.setTkey(req.tkey());
      keyRepo.save(key);

      // Remove old values
      valueRepo.deleteAll(
          valueRepo.findAll().stream().filter(v -> v.getKey().getId().equals(id)).toList()
      );

      // Insert new values
      for (var v : req.values()) {
          LocaleEntity loc = localeRepo.findByCode(v.locale())
              .orElseGet(() -> localeRepo.save(new LocaleEntity(v.locale())));
          TranslationValue tv = new TranslationValue();
          tv.setKey(key);
          tv.setLocale(loc);
          tv.setPlatform(v.platform());
          tv.setText(v.text());
          valueRepo.save(tv);
      }

      // Update tags
      keyTagRepo.deleteAll(keyTagRepo.findAll().stream()
          .filter(kt -> kt.getKeyId().equals(id)).toList());
      if (req.tags() != null) {
          for (String name : req.tags()) {
              Tag t = tagRepo.findByName(name).orElseGet(() -> tagRepo.save(new Tag(name)));
              keyTagRepo.save(new KeyTag(key.getId(), t.getId()));
          }
      }
  }

  @Transactional
  public void delete(Long id) {
      keyRepo.deleteById(id);
  }
}
