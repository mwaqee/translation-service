package org.mwtech.translation.api.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record CreateTranslationRequest(
  String namespace,
  @NotBlank String tkey,
  @NotEmpty List<ValueItem> values,
  List<String> tags
){
  public record ValueItem(@NotBlank String locale, @NotBlank String platform, @NotBlank String text) {}
}
