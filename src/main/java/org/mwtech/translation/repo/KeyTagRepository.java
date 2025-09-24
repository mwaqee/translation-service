package org.mwtech.translation.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.mwtech.translation.domain.KeyTag;

public interface KeyTagRepository extends JpaRepository<KeyTag, org.mwtech.translation.domain.KeyTag.KeyTagId> { }
