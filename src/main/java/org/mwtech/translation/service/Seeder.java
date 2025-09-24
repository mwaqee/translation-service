package org.mwtech.translation.service;

import org.mwtech.translation.domain.LocaleEntity;
import org.mwtech.translation.repo.LocaleRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Seeder {
  private final JdbcTemplate jdbc;
  private final LocaleRepository localeRepo;
  public Seeder(JdbcTemplate jdbc, LocaleRepository localeRepo){ this.jdbc=jdbc; this.localeRepo=localeRepo; }

  public void seed(int numValues){
    // Ensure locales
    for (String code: new String[]{"en","fr","es"}) {
      localeRepo.findByCode(code).orElseGet(() -> localeRepo.save(new LocaleEntity(code)));
    }
    // Create keys
    int keys = Math.max(1, numValues/2);
    jdbc.batchUpdate("INSERT INTO translation_keys(namespace,tkey,created_at,updated_at) VALUES (?,?,now(),now())",
      new org.springframework.jdbc.core.BatchPreparedStatementSetter(){
        public void setValues(java.sql.PreparedStatement ps, int i) throws java.sql.SQLException{
          ps.setString(1, "ns"+(i%10));
          ps.setString(2, "key_"+i);
        }
        public int getBatchSize(){ return keys; }
      });
    // Insert values
    Random rnd = new Random(42);
    jdbc.batchUpdate("INSERT INTO translation_values(key_id,locale_id,platform,text,created_at,updated_at) VALUES (?,?,?,?,now(),now())",
      new org.springframework.jdbc.core.BatchPreparedStatementSetter(){
        public void setValues(java.sql.PreparedStatement ps, int i) throws java.sql.SQLException{
          long keyId = 1 + (i % keys);
          long localeId = 1 + (i % 3);
          String platform = (i%2==0) ? "web" : "mobile";
          String text = "Value "+i+" lorem "+Integer.toHexString(rnd.nextInt(1_000_000));
          ps.setLong(1, keyId);
          ps.setLong(2, localeId);
          ps.setString(3, platform);
          ps.setString(4, text);
        }
        public int getBatchSize(){ return numValues; }
      });
  }
}
