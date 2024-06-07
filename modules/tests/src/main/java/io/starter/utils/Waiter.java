package io.starter.utils;

import java.net.ConnectException;
import java.time.Duration;
import java.util.concurrent.Callable;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;

public class Waiter {

  public static void awaitCondition(Callable<Boolean> condition, String errorMessage,
                                    Duration timeout, Duration pollingInterval) {
    try {
      Awaitility.await().atMost(timeout)
          .pollInterval(pollingInterval)
          .pollDelay(Duration.ZERO)
          .pollInSameThread()
          .ignoreException(ConnectException.class)
          .until(condition);
    } catch (ConditionTimeoutException conditionTimeoutException) {
      throw buildConditionTimeoutException(errorMessage, timeout.getSeconds(), conditionTimeoutException);
    }
  }

  private static ConditionTimeoutException buildConditionTimeoutException(
      String errorMessage,
      long timeoutSeconds,
      ConditionTimeoutException conditionTimeoutException) {
    return new ConditionTimeoutException(
        "%s, within '%d' seconds".formatted(errorMessage, timeoutSeconds),
        conditionTimeoutException);
  }
}
