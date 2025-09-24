package org.mwtech.translation.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="translation_keys", uniqueConstraints = @UniqueConstraint(columnNames={"namespace","tkey"}))
public class TranslationKey {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String namespace;
  @Column(nullable=false) private String tkey;
  @Column(nullable=false) private Instant createdAt;
  @Column(nullable=false) private Instant updatedAt;

  @PrePersist void prePersist(){ createdAt = updatedAt = Instant.now(); }
  @PreUpdate  void preUpdate(){ updatedAt = Instant.now(); }

  public Long getId(){return id;}
  public String getNamespace(){return namespace;}
  public void setNamespace(String namespace){this.namespace=namespace;}
  public String getTkey(){return tkey;}
  public void setTkey(String tkey){this.tkey=tkey;}
  public Instant getCreatedAt(){return createdAt;}
  public Instant getUpdatedAt(){return updatedAt;}
}
