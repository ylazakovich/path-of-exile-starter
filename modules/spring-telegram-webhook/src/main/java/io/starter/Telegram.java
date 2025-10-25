package io.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Telegram {

  static void main(String[] args) {
    SpringApplication.run(Telegram.class, args);
  }
}
