package io.automation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.automation.entity.GemEntity;

public class GemDTO implements DTO {

  @JsonProperty("lines")
  private List<Gem> lines;

  public List<Gem> getLines() {
    return lines;
  }

  public GemDTO(List<GemEntity> entities) {
    this.lines = entities.stream()
        .map(entity -> new Gem()
            .setName(entity.getName())
            .setVariant(entity.getVariant())
            .setGemLevel(entity.getGemLevel())
            .setGemQuality(entity.getGemQuality())
            .setCorrupted(entity.isCorrupted())
            .setChaosValue(entity.getChaosValue()))
        .collect(Collectors.toList());
  }

  public GemDTO setLines(List<Gem> lines) {
    this.lines = lines;
    return this;
  }

  public static List<GemEntity> convertToEntity(List<Gem> data) {;
    return data.stream()
        .map(gem -> new GemEntity(
            gem.getName(),
            gem.getVariant(),
            gem.isCorrupted(),
            gem.getGemLevel(),
            gem.getGemQuality(),
            gem.getChaosValue()))
        .collect(Collectors.toList());
  }

  public static class Gem {

    @JsonProperty("name")
    private String name;
    @JsonProperty("variant")
    private String variant;
    @JsonProperty("corrupted")
    private boolean corrupted;
    @JsonProperty("gemLevel")
    private int gemLevel;
    @JsonProperty("gemQuality")
    private int gemQuality;
    @JsonProperty("chaosValue")
    private double chaosValue;

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
  }
}
