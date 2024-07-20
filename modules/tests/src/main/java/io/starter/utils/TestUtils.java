package io.starter.utils;

import java.lang.annotation.Annotation;

import lombok.experimental.UtilityClass;
import org.testng.ITestNGMethod;

@UtilityClass
public class TestUtils {

  public static <T extends Annotation> T getTestAnnotation(ITestNGMethod test, Class<T> annotationType) {
    return test.getConstructorOrMethod().getMethod().getAnnotation(annotationType);
  }
}
