package org.mwtech.translation.api;

import org.mwtech.translation.api.dto.CreateTranslationRequest;
import org.mwtech.translation.api.dto.SearchItem;
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
  public Page<SearchItem> search(
      @RequestParam(name = "namespace", required = false) String namespace,
      @RequestParam(name = "locale", required = false) String locale,
      @RequestParam(name = "platform", required = false) String platform,
      @RequestParam(name = "q", required = false) String q,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "50") int size
  ){
    var p = svc.search(namespace, locale, platform, q, PageRequest.of(page, Math.min(size, 200)));
    return p.map(tv -> new SearchItem(
        tv.getId(),
        tv.getKey().getNamespace(),
        tv.getKey().getTkey(),
        tv.getLocale().getCode(),
        tv.getPlatform(),
        tv.getText()
    ));
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
