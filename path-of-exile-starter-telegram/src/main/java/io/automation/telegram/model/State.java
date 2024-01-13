package io.automation.telegram.model;

public enum State {
  NO_COMMAND,
  START,
  SKILL_EVENT,
  ENTER_DESCRIPTION, //the bot will wait for the description to be entered.
  EDIT_DESCRIPTION, //the bot will wait for the description to be entered
  CREATE_EVENT, //the bot run created event
  ALL_EVENTS, //show all events
  MY_EVENTS, //the bot show to user list events.
  ENTER_NUMBER_EVENT, //the bot will wait for the number of event to be entered.
  ENTER_NUMBER_FOR_EDIT, //the bot will wait for the number of event to be entered
  ENTER_DATE, //the bot will wait for the date to be entered
  EDIT_DATE, //the bot will wait for the date to be entered
  EDIT_FREQ, //the bot will wait callbackquery
  ALL_USERS, // show all users
  ENTER_NUMBER_USER, //the bot will wait for the number of user to be entered.
  ENTER_TIME, //the bot will wait for the hour to be entered.
  ON_EVENT // state toggle
}
