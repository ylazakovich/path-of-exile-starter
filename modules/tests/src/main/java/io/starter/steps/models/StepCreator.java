package io.starter.steps.models;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import com.codeborne.selenide.Selenide;
import lombok.SneakyThrows;

public interface StepCreator {

  /**
   * Get page steps from given step class.
   *
   * @param stepClass page steps
   * @param <T>       like {@link PageSteps}
   * @return expected page steps
   */
  @SneakyThrows({NoSuchMethodException.class, InvocationTargetException.class, IllegalAccessException.class, InstantiationException.class})
  default <T extends PageSteps<?, ?, ?>> T getPageSteps(Class<T> stepClass) {
    Class<?> pageClass = getPageClass(stepClass);
    return stepClass.getConstructor(pageClass).newInstance(Selenide.page(pageClass.getConstructor().newInstance()));
  }

  /**
   * Get page class from given step class.
   *
   * @param stepClass page steps
   * @param <T>       like {@link PageSteps}
   * @return expected page class
   */
  default <T extends PageSteps<?, ?, ?>> Class<?> getPageClass(Class<T> stepClass) {
    int expectedConstructorParamsCount = 1;
    int pageParamIndex = 0;
    return Arrays.stream(stepClass.getConstructors())
        .filter(constructor ->
            constructor.getParameterCount() == expectedConstructorParamsCount
                && Page.class.isAssignableFrom(constructor.getParameterTypes()[pageParamIndex])
        ).findFirst()
        .orElseThrow(() -> new RuntimeException("No valid constructor in given step class"))
        .getParameterTypes()[pageParamIndex];
  }
}
