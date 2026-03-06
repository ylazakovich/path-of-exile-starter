package io.starter.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "vendor-recipes")
public class VendorRecipeConfiguration {

  private List<RecipeDefinition> definitions = new ArrayList<>();

  @Data
  public static class RecipeDefinition {
    private String id;
    private String name;
    private ResultDefinition result;
    private List<IngredientDefinition> ingredients = new ArrayList<>();
  }

  @Data
  public static class IngredientDefinition {
    private String name;
    private Integer quantity = 1;
    private PriceSource priceSource = PriceSource.AUTO;
  }

  @Data
  public static class ResultDefinition {
    private String name;
    private PriceSource priceSource = PriceSource.AUTO;
  }

  public enum PriceSource {
    AUTO,
    UNIQUE_ITEM,
    CURRENCY,
    SKILL_GEM
  }
}
