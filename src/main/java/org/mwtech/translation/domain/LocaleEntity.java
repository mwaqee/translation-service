package org.mwtech.translation.domain;

import jakarta.persistence.*;

@Entity
@Table(name="locales")
public class LocaleEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable=false, unique=true) private String code;

  public LocaleEntity(){}
  public LocaleEntity(String code){this.code=code;}
  public Long getId(){return id;}
  public String getCode(){return code;}
  public void setCode(String code){this.code=code;}
}
