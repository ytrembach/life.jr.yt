package org.yt.jr.projects.utils.logs;

public enum LogSources {
    SYSTEM("system"),
    CREATURE("creature");

    final private String message;

    LogSources(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
