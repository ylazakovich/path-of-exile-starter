package io.starter.config;

public class ScheduleConfig {

  public static final String A8R_SKILLS_ADD_CRON = "${A8R_SKILLS_ADD_CRON:5 */2 * * * *}";
  public static final String A8R_SKILLS_UPDATE_CRON = "${A8R_SKILLS_UPDATE_CRON:0 */5 * * * *}";

  public static final String A8R_CURRENCIES_UPDATE_CRON = "${A8R_CURRENCIES_UPDATE_CRON:15 */2 * * * *}";

  public static final String A8R_PROCESSED_SKILLS_ADD_CRON = "${A8R_PROCESSED_SKILLS_ADD_CRON:25 */2 * * * *}";
  public static final String A8R_PROCESSED_SKILLS_UPDATE_CRON = "${A8R_PROCESSED_SKILLS_UPDATE_CRON:20 */5 * * * *}";

  public static final String A8R_UNIQUE_JEWELS_ADD_CRON = "${A8R_UNIQUE_JEWELS_ADD_CRON:35 */2 * * * *}";
  public static final String A8R_UNIQUE_JEWELS_UPDATE_CRON = "${A8R_UNIQUE_JEWELS_UPDATE_CRON:30 */5 * * * *}";

  public static final String A8R_VENDOR_RECIPES_UPDATE_CRON = "${A8R_VENDOR_RECIPES_UPDATE_CRON:10 */5 * * * *}";
}
