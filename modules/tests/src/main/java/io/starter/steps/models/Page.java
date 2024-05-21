package io.starter.steps.models;

import io.starter.annotations.PageUrl;

/**
 * Interface for Ui page class.
 */
public interface Page {

  /**
   * Get url.
   *
   * @return url as {@link String}
   */
  default String getUrl() {
    return getClass().getAnnotation(PageUrl.class).value();
  }
}
