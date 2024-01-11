package org.yt.jr.projects.utils.logs;

public enum LogSources {
    SYSTEM("system"),
    CREATURE("creature"),
    LIFECYCLE("lifecycle"),
    LOCATION("action");

    final private String message;

    LogSources(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
