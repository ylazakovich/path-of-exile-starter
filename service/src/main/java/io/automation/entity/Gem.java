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
public class Gem implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false)
  private Long id;
  private String name;
  private String variant;
  private boolean corrupted;
  private int gemLevel;
  private int gemQuality;
  private double chaosValue;

  public Gem() {
  }

  public Gem(long id,
             String name,
             String variant,
             boolean corrupted,
             int gemLevel,
             int gemQuality,
             double chaosValue) {
    this.id = id;
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

  public Gem setId(long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Gem setName(String name) {
    this.name = name;
    return this;
  }

  public String getVariant() {
    return variant;
  }

  public Gem setVariant(String variant) {
    this.variant = variant;
    return this;
  }

  public boolean isCorrupted() {
    return corrupted;
  }

  public Gem setCorrupted(boolean corrupted) {
    this.corrupted = corrupted;
    return this;
  }

  public int getGemLevel() {
    return gemLevel;
  }

  public Gem setGemLevel(int gemLevel) {
    this.gemLevel = gemLevel;
    return this;
  }

  public int getGemQuality() {
    return gemQuality;
  }

  public Gem setGemQuality(int gemQuality) {
    this.gemQuality = gemQuality;
    return this;
  }

  public double getChaosValue() {
    return chaosValue;
  }

  public Gem setChaosValue(double chaosValue) {
    this.chaosValue = chaosValue;
    return this;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    Gem gem = (Gem) object;
    return id == gem.id && corrupted == gem.corrupted && gemLevel == gem.gemLevel && gemQuality == gem.gemQuality &&
        Double.compare(chaosValue, gem.chaosValue) == 0 && Objects.equals(name, gem.name) &&
        Objects.equals(variant, gem.variant);
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
}
