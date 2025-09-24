package org.mwtech.translation.service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExportService {
  private final TranslationService svc;
  private final Map<String, Cached> cache = new ConcurrentHashMap<>();

  public ExportService(TranslationService svc){ this.svc = svc; }

  public ResponseEntity<Map<String,String>> export(String locale, String platform, String ns,
                                                   String ifNoneMatch, Instant ifModifiedSince){
    String key = (ns==null?"*":ns) + "|" + locale + "|" + platform;
    Instant ver = svc.lastUpdated();
    String etag = "\""+ key + "|" + ver.toEpochMilli() + "\"";

    Cached c = cache.compute(key,(k,old)-> build(ns, locale, platform, etag, ver));

    if (ifNoneMatch!=null && etag.equals(ifNoneMatch)) {
      return ResponseEntity.status(304).eTag(etag).lastModified(ver.toEpochMilli()).build();
    }
    return ResponseEntity.ok().eTag(etag).lastModified(ver.toEpochMilli()).body(c.map());
  }

  private Cached build(String ns, String locale, String platform, String etag, Instant ver){
    Map<String,String> map = svc.export(locale, platform, ns);
    return new Cached(map, etag, ver);
  }

  private record Cached(Map<String,String> map, String etag, Instant lastMod) {}
}
