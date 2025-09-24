package org.mwtech.translation.repo;

import org.mwtech.translation.domain.TranslationValue;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface TranslationValueRepository extends JpaRepository<TranslationValue, Long> {

  @Query("""
   select tv from TranslationValue tv
   join tv.key k
   join tv.locale lc
   where (:namespace is null or k.namespace = :namespace)
     and (:locale is null or lc.code = :locale)
     and (:platform is null or tv.platform = :platform)
     and ( :q is null
           or lower(k.tkey) like lower(concat('%', :q, '%'))
           or lower(tv.text) like lower(concat('%', :q, '%')) )
  """)
  Page<TranslationValue> search(String namespace, String locale, String platform, String q, Pageable pageable);

  @Query("""
    select k.tkey, tv.text from TranslationValue tv
    join tv.key k join tv.locale lc
    where lc.code = :locale and tv.platform = :platform
      and (:namespace is null or k.namespace = :namespace)
  """)
  List<Object[]> exportRows(String locale, String platform, String namespace);

  @Query("select max(tv.updatedAt) from TranslationValue tv")
  Instant maxUpdated();
}
