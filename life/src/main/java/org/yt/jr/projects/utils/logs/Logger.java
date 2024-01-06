package org.yt.jr.projects.utils.logs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger  {
    private static Logger LOGGER = null;

    private static BufferedWriter writer;

    private Logger(String path) {
        try {
            writer = new BufferedWriter(new FileWriter(path, true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initLogger(String path) {
        if (LOGGER == null) {
            LOGGER = new Logger(path);
        }
        Log(LogSources.SYSTEM, LogLevels.INFO, "started");
    }

    public static void closeLogger() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void Log(LogSources source, LogLevels level, String message) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String logMessage = String.format("%s %s %s: %s\n",
                localDateTime.format(DateTimeFormatter.ISO_DATE_TIME),
                source.getMessage(),
                level.getMessage(),
                message);
        try {
            writer.write(logMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
