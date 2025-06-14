package io.starter.component;

import io.starter.controller.DatabaseController;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitLoader implements ApplicationRunner {

  private final DatabaseController controller;

  @Override
  public void run(ApplicationArguments args) {
    controller.loading();
  }
}
