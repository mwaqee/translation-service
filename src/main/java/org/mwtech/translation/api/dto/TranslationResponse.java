package org.mwtech.translation.api.dto;
import java.time.Instant;
import java.util.List;
import java.util.Map;
public record TranslationResponse(
  Long id, String namespace, String tkey,
  Map<String, Map<String,String>> texts,
  List<String> tags,
  Instant updatedAt
) {}
