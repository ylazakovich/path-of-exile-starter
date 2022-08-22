package io.automation.telegram.model;

public enum BotState {
    ENTERDESCRIPTION,//the bot will wait for the description to be entered.
    START,
    MYEVENTS, //the bot show to user list events.
    ENTERNUMBEREVENT,//the bot will wait for the number of event to be entered.
    ENTERDATE, //the bot will wait for the date to be entered
    CREATE, //the bot run created event
    ENTERNUMBERFOREDIT, //the bot will wait for the number of event to be entered
    EDITDATE, //the bot will wait for the date to be entered
    EDITDESCRIPTION,//the bot will wait for the description to be entered
    EDITFREQ,//the bot will wait callbackquery
    ALLUSERS, // show all users
    ALLEVENTS, //show all events
    ENTERNUMBERUSER,//the bot will wait for the number of user to be entered.
    ENTERTIME,//the bot will wait for the hour to be entered.
    ONEVENT // state toggle
}
