package io.automation.telegram.model;

public enum EventFreq {
  TIME, //will work once and leave
  EVERYDAY,//will work every day at a certain hour
  MONTH,//will work every month on the set day and hour
  YEAR//will be triggered every year at the specified month, day and hour
}
