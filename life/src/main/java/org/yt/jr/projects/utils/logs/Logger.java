package org.yt.jr.projects.utils.logs;

import org.yt.jr.projects.utils.Config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static boolean initialized = false;
    private static BufferedWriter writer;

    public static void initLogger(String path) {
        if (!initialized) {
            try {
                writer = new BufferedWriter(new FileWriter(path, true));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            initialized = true;
            Log(LogSources.SYSTEM, LogLevels.INFO, "logger started");
        } else {
            Log(LogSources.SYSTEM, LogLevels.ERROR, "logger already started");
        }
    }

    public static void closeLogger() {
        Log(LogSources.SYSTEM, LogLevels.INFO, "logger stopped");
        initialized = false;
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Log(LogSources source, LogLevels level, String message) {
        if (level.ordinal() < Config.CONFIG.getLogLevel().ordinal()) {
            return;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        String logMessage = String.format("%s %s %s: %s\n",
                localDateTime.format(DateTimeFormatter.ISO_DATE_TIME),
                source.getMessage(),
                level.getMessage(),
                message);

        if (!initialized) {
            throw new RuntimeException(String.format("Logger doesn't initialized yet, it cannot log this message: %s",
                    logMessage));
        }
        try {
            writer.write(logMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
