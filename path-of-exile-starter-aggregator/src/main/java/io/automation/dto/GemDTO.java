package io.automation.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.automation.entity.GemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class GemDTO {

  @JsonProperty("lines")
  private List<Gem> lines;

  public GemDTO(List<GemEntity> entities) {
    this.lines = entities.stream()
        .map(entity -> new Gem(
            entity.getName(),
            entity.getVariant(),
            entity.isCorrupted(),
            entity.getGemLevel(),
            entity.getGemQuality(),
            entity.getChaosValue()
        ))
        .collect(Collectors.toList());
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

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode
  @ToString
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
  }
}
