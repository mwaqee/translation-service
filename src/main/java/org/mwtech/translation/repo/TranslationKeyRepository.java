package org.mwtech.translation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.mwtech.translation.domain.TranslationKey;
import java.util.Optional;

public interface TranslationKeyRepository extends JpaRepository<TranslationKey, Long> {
  Optional<TranslationKey> findByNamespaceAndTkey(String namespace, String tkey);
}
