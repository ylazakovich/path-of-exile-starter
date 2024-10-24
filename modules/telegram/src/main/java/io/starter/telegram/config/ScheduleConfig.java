package io.starter.telegram.config;

public class ScheduleConfig {

  public static final String T6M_ADD_CRON = "${T6M_ADD_CRON:0 */2 * * * *}";
  public static final String T6M_UPDATE_CRON = "${T6M_UPDATE_CRON:0 */5 * * * *}";
}
