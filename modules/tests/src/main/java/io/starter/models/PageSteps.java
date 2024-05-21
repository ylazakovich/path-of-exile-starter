package io.starter.models;

import io.qameta.allure.Step;
import io.starter.utils.Browser;

/**
 * Abstract class for page steps class.
 *
 * @param <S> steps class
 * @param <V> verification steps class
 * @param <P> page class
 */
public abstract class PageSteps<S extends PageSteps<S, V, P>, V extends Verification<S, P>, P extends Page>
    implements StepCreator {

  protected V verification;
  protected P page;

  /**
   * Start verification chain.
   *
   * @return verification steps class
   */
  public V verify() {
    return verification;
  }

  /**
   * Refresh current page.
   *
   * @return steps class
   */
  @Step("Refresh page")
  public S refreshPage() {
    Browser.refresh();
    return (S) this;
  }

  /**
   * Back to previous page.
   *
   * @param stepClass steps class
   * @param <T>       like {@link PageSteps}
   * @return expected page steps
   */
  @Step("Back to previous page")
  public <T extends PageSteps<?, ?, ?>> T backToPreviousPage(Class<T> stepClass) {
    Browser.back();
    return getPageSteps(stepClass);
  }

  /**
   * Navigate to specific page.
   *
   * @param navigationSteps expected navigation steps
   * @param <T>             like {@link Navigation}
   * @return navigation page steps
   */
  public <T extends Navigation> T navigate(T navigationSteps) {
    return navigationSteps;
  }
}
