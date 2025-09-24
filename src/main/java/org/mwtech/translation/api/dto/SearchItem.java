package org.mwtech.translation.api.dto;

public record SearchItem(
    Long id,
    String namespace,
    String tkey,
    String locale,
    String platform,
    String text
) {}
