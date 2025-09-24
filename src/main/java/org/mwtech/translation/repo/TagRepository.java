package org.mwtech.translation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.mwtech.translation.domain.Tag;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
  Optional<Tag> findByName(String name);
}
