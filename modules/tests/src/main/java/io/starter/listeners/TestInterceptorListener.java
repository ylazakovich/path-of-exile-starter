package io.starter.listeners;

import java.util.List;
import java.util.Objects;

import io.starter.annotations.Nightly;
import io.starter.config.AppConfig;
import io.starter.utils.TestUtils;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

public class TestInterceptorListener implements IMethodInterceptor {

  @Override
  public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
    if (!AppConfig.IS_NIGHTLY_RUN) return methods;
    return methods.stream()
        .filter(methodInstance -> needRunTest(TestUtils.getTestAnnotation(methodInstance.getMethod(), Nightly.class)))
        .toList();
  }

  private boolean needRunTest(Nightly annotation) {
    return !Objects.isNull(annotation) && !annotation.isEnabled();
  }
}
