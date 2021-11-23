package com.github.arkronzxc.chat.server.chat;

import java.time.LocalTime;

public class Entry {
    private final String message;
    private final LocalTime time;

    public Entry(String message) {
        this.message = message;
        this.time = LocalTime.now();
    }

    public LocalTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
