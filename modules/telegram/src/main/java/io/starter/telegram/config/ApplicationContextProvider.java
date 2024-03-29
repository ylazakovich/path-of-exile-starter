package io.starter.telegram.config;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

  @Getter
  private static ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext context)
      throws BeansException {
    ApplicationContextProvider.context = context;
  }
}
