package org.yt.jr.projects.utils.logs;

import org.yt.jr.projects.utils.Config;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static boolean initialized = false;
    private static FileWriter writer;

    public static void initLogger(Path path) {
        if (!initialized) {
            try {
                writer = new FileWriter(path.toFile(), false);
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
        if (level.ordinal() < Config.CONFIG.logLevel().ordinal()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String logMessage = String.format("%s %s%s %s %s: %s\n",
                now.format(DateTimeFormatter.ISO_DATE_TIME),
                Thread.currentThread().getName(),
                Thread.currentThread().getId(),
                source.getMessage(),
                level.getMessage(),
                message);

        if (level == LogLevels.FATAL) {
            System.out.println(message);
        }

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
