package io.starter.component;

import java.util.concurrent.atomic.AtomicBoolean;

import io.starter.config.ApplicationConfiguration;

import org.aeonbits.owner.ConfigCache;
import org.springframework.stereotype.Component;

@Component
public class StartupReadiness {

  private static final ApplicationConfiguration CONFIG =
      ConfigCache.getOrCreate(ApplicationConfiguration.class);

  private final AtomicBoolean ready = new AtomicBoolean(!CONFIG.startupLoaderEnabled());

  public boolean isReady() {
    return ready.get();
  }

  public void markReady() {
    ready.set(true);
  }

  public void markNotReady() {
    ready.set(false);
  }
}
