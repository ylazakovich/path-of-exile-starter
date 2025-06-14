package io.starter.config;

public class ScheduleConfig {

  public static final String A8R_ADD_CRON = "${A8R_ADD_CRON:0 */2 * * * *}";
  public static final String A8R_UPDATE_CRON = "${A8R_UPDATE_CRON:0 */5 * * * *}";
}
