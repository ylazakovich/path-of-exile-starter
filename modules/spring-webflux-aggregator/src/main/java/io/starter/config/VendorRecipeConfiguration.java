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
    private String resultItemName;
    private List<String> ingredients = new ArrayList<>();
  }
}
