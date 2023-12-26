package io.automation.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gems")
public class GemEntity implements Serializable, io.automation.entity.Entity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, updatable = false)
  private Long id;
  private String name;
  private String variant;
  private boolean corrupted;
  private int gemLevel;
  private int gemQuality;
  private double chaosValue;

  public GemEntity() {
  }

  public GemEntity(String name,
                   String variant,
                   boolean corrupted,
                   int gemLevel,
                   int gemQuality,
                   double chaosValue) {
    this.name = name;
    this.variant = variant;
    this.corrupted = corrupted;
    this.gemLevel = gemLevel;
    this.gemQuality = gemQuality;
    this.chaosValue = chaosValue;
  }

  public long getId() {
    return id;
  }

  public GemEntity setId(long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public GemEntity setName(String name) {
    this.name = name;
    return this;
  }

  public String getVariant() {
    return variant;
  }

  public GemEntity setVariant(String variant) {
    this.variant = variant;
    return this;
  }

  public boolean isCorrupted() {
    return corrupted;
  }

  public GemEntity setCorrupted(boolean corrupted) {
    this.corrupted = corrupted;
    return this;
  }

  public int getGemLevel() {
    return gemLevel;
  }

  public GemEntity setGemLevel(int gemLevel) {
    this.gemLevel = gemLevel;
    return this;
  }

  public int getGemQuality() {
    return gemQuality;
  }

  public GemEntity setGemQuality(int gemQuality) {
    this.gemQuality = gemQuality;
    return this;
  }

  public double getChaosValue() {
    return chaosValue;
  }

  public GemEntity setChaosValue(double chaosValue) {
    this.chaosValue = chaosValue;
    return this;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    GemEntity gemEntity = (GemEntity) object;
    return id == gemEntity.id && corrupted == gemEntity.corrupted && gemLevel == gemEntity.gemLevel && gemQuality == gemEntity.gemQuality &&
        Double.compare(chaosValue, gemEntity.chaosValue) == 0 && Objects.equals(name, gemEntity.name) &&
        Objects.equals(variant, gemEntity.variant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, variant, corrupted, gemLevel, gemQuality, chaosValue);
  }

  @Override
  public String toString() {
    return "Gem{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", variant='" + variant + '\'' +
        ", corrupted=" + corrupted +
        ", gemLevel=" + gemLevel +
        ", gemQuality=" + gemQuality +
        ", chaosValue=" + chaosValue +
        '}';
  }

  @Override
  public io.automation.entity.Entity getEntity() {
    return this;
  }
}
