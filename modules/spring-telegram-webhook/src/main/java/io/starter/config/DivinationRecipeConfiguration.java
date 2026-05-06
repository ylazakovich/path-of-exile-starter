package io.starter.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "divination-recipes")
public class DivinationRecipeConfiguration {

  private List<RecipeDefinition> definitions = new ArrayList<>();

  @Data
  public static class RecipeDefinition {
    private String id;
    private String cardName;
    private Integer stackSize = 1;
    private String resultName;
    private Integer resultQuantity = 1;
  }
}
