package io.automation.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Lines<T> {

  @JsonProperty("lines")
  private List<T> lines;

  public Lines<T> setLines(List<T> lines) {
    this.lines = lines;
    return this;
  }
}
