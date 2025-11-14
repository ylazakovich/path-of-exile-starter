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

  @Override
  public void run(ApplicationArguments args) {
    if (CONFIG.startupLoaderEnabled()) {
      LOG.info("🚀 Starting full init...");
      startupLoader.loadEverything();
      LOG.info("✅ Full init completed.");
    }
  }
}
