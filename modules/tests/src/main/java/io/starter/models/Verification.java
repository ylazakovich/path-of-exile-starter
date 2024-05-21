package io.starter.models;

import lombok.AllArgsConstructor;

/**
 * Abstract class for verification steps.
 *
 * @param <S> steps class
 * @param <P> page class
 */
@AllArgsConstructor
public abstract class Verification<S extends PageSteps<S, ?, ?>, P extends Page> {

  private S steps;
  protected P page;

  /**
   * Return to steps chain.
   *
   * @return steps class
   */
  public S backToSteps() {
    return steps;
  }
}
