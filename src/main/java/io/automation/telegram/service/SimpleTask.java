package io.automation.telegram.service;

import java.util.TimerTask;

//task wrapper
public class SimpleTask extends TimerTask {
    private final SendEvent sendEvent;

    public SimpleTask(SendEvent sendEvent) {
        this.sendEvent = sendEvent;
    }

    @Override
    public void run() {
        sendEvent.start();
    }
}
