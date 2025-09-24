package org.mwtech.translation.api;

import org.mwtech.translation.api.dto.CreateTranslationRequest;
import org.mwtech.translation.domain.TranslationValue;
import org.mwtech.translation.service.TranslationService;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/translations")
public class TranslationController {
  private final TranslationService svc;
  public TranslationController(TranslationService svc){ this.svc = svc; }

  @PostMapping
  public ResponseEntity<Long> create(@Validated @RequestBody CreateTranslationRequest req){
    return ResponseEntity.ok(svc.create(req));
  }

  @GetMapping
  public Page<TranslationValue> search(
      @RequestParam(required=false) String namespace,
      @RequestParam(required=false) String locale,
      @RequestParam(required=false) String platform,
      @RequestParam(required=false) String q,
      @RequestParam(defaultValue="0") int page,
      @RequestParam(defaultValue="50") int size){
    return svc.search(namespace, locale, platform, q, PageRequest.of(page, Math.min(size, 200)));
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
      @PathVariable Long id,
      @Validated @RequestBody CreateTranslationRequest req
  ) {
      svc.update(id, req);
      return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
      svc.delete(id);
      return ResponseEntity.noContent().build();
  }
}
