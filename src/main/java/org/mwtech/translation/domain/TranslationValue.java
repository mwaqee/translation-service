package org.mwtech.translation.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="translation_values", uniqueConstraints=@UniqueConstraint(columnNames={"key_id","locale_id","platform"}))
public class TranslationValue {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="key_id", nullable=false)
  private TranslationKey key;

  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="locale_id", nullable=false)
  private LocaleEntity locale;

  @Column(nullable=false) private String platform;
  @Column(nullable=false, columnDefinition="text") private String text;
  @Column(nullable=false) private Instant createdAt;
  @Column(nullable=false) private Instant updatedAt;

  @PrePersist void prePersist(){ createdAt = updatedAt = Instant.now(); }
  @PreUpdate  void preUpdate(){ updatedAt = Instant.now(); }

  public Long getId(){return id;}
  public TranslationKey getKey(){return key;}
  public void setKey(TranslationKey key){this.key=key;}
  public LocaleEntity getLocale(){return locale;}
  public void setLocale(LocaleEntity locale){this.locale=locale;}
  public String getPlatform(){return platform;}
  public void setPlatform(String platform){this.platform=platform;}
  public String getText(){return text;}
  public void setText(String text){this.text=text;}
  public Instant getUpdatedAt(){return updatedAt;}
}
