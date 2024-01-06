package org.yt.jr.projects.utils.logs;

public enum LogLevels {
    DEBUG("debug"),
    INFO("info"),
    ERROR("error"),
    FATAL("fatal");

    private String message;

    LogLevels(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
