package io.starter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PoeNinjaConfig {

  @Value("${aggregator.ninja.base-url}")
  public String baseUrl;
  @Value("${aggregator.ninja.route}")
  public String route;
  @Value("${aggregator.ninja.league}")
  public String league;
}
