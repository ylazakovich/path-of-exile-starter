package io.starter;

import io.starter.controller.DatabaseController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AggregatorApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(AggregatorApplication.class, args);
    context.getBean(DatabaseController.class).loadAllLeagues();
    context.getBean(DatabaseController.class).loadAllSkills();
  }
}
