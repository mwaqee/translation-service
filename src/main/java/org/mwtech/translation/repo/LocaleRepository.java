package org.mwtech.translation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.mwtech.translation.domain.LocaleEntity;
import java.util.Optional;

public interface LocaleRepository extends JpaRepository<LocaleEntity, Long> {
  Optional<LocaleEntity> findByCode(String code);
}
