package io.starter;

import io.starter.telegram.service.DatabaseService;
import io.starter.telegram.service.WebhookService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TelegramApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(TelegramApplication.class, args);
    context.getBean(WebhookService.class).assignToken();
    context.getBean(DatabaseService.class).loadSkills();
  }
}
