package io.starter.steps.models;

import io.starter.annotations.PageTitle;
import io.starter.annotations.PageUrl;

/**
 * Interface for Ui page class.
 */
public interface Page {

  /**
   * Get title of page.
   *
   * @param args dynamic parts in url
   * @return title as {@link String}
   */
  default String getTitle(Object... args) {
    return getTitle().formatted(args);
  }

  /**
   * Get title of page.
   *
   * @return title as {@link String}
   */
  default String getTitle() {
    return getClass().getAnnotation(PageTitle.class).value();
  }


  /**
   * Get url with dynamic parts.
   *
   * @param args dynamic parts in url
   * @return url with dynamic parts as {@link String}
   */
  default String getUrl(Object... args) {
    return getUrl().formatted(args);
  }

  /**
   * Get url.
   *
   * @return url as {@link String}
   */
  default String getUrl() {
    return getClass().getAnnotation(PageUrl.class).value();
  }
}
