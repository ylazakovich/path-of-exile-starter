package io.starter.component;

import io.starter.config.ApplicationConfiguration;

import lombok.RequiredArgsConstructor;
import org.aeonbits.owner.ConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitLoader implements ApplicationRunner {

  private static final ApplicationConfiguration CONFIG = ConfigCache.getOrCreate(ApplicationConfiguration.class);
  private static final Logger LOG = LoggerFactory.getLogger(InitLoader.class);
  private final StartupLoader startupLoader;
  private final StartupReadiness startupReadiness;

  @Override
  public void run(ApplicationArguments args) {
    if (CONFIG.startupLoaderEnabled()) {
      startupReadiness.markNotReady();
      LOG.info("🚀 Starting full init...");
      try {
        startupLoader.loadEverything();
      } catch (InterruptedException e) {
        LOG.error("Startup interrupted.", e);
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
      startupReadiness.markReady();
      LOG.info("✅ Full init completed.");
    } else {
      startupReadiness.markReady();
    }
  }
}
