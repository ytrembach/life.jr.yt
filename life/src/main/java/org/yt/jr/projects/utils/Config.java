package org.yt.jr.projects.utils;

import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleType;
import org.yt.jr.projects.utils.logs.LogLevels;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    final private static String PROPERTIES_FILENAME = "life.properties";
    final private static String LOGFILE_NAME = "life.log";

    final private Properties properties;

    public Config(final String propertiesFileName) {
        try {
            final String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            final String appConfigPath = rootPath + propertiesFileName;
            properties = new Properties();
            properties.load(new FileInputStream(appConfigPath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    final public static Config CONFIG = new Config(PROPERTIES_FILENAME);

    // System
    public Path logFile() {
        return Path.of(properties.getProperty("system.log.path"), LOGFILE_NAME);
    }

    public LogLevels logLevel() {
        return LogLevels.valueOf(properties.getProperty("system.log.level").toUpperCase());
    }

    public int mapCols() {
        return Integer.parseInt(properties.getProperty("system.map.cols"));
    }

    public int mapRows() {
        return Integer.parseInt(properties.getProperty("system.map.rows"));
    }

    public int turnDurationInSeconds() {
        return Integer.parseInt(properties.getProperty("system.turn.duration_in_seconds"));
    }

    // Life Cycle
    public int poolSize(LifeCycleType type) {
        return Integer.parseInt(get(type, "pool.size"));
    }

    public int turnsPerMove(LifeCycleType type) {
        return Integer.parseInt(get(type, "turns_per_move"));
    }

    // Creatures
    public int creatureDefaultHealth(String key) {
        return Integer.parseInt(get(CreatureType.NONEXISTENT, "health." + key));
    }

    public int maxCreaturePerLocation(CreatureType type) {
        return Integer.parseInt(get(type, "per_location.max"));
    }

    public Double initCreaturePerLocationShare(CreatureType type) {
        return Double.parseDouble(get(type, "per_location.init"));
    }

    public int turnsToReproduce(CreatureType type) {
        return Integer.parseInt(get(type, "turns_per_reproduce"));
    }

    public Double reproduceProbability(CreatureType type) {
        return Double.parseDouble(get(type, "reproduce_probability"));
    }

    public int maxAge(CreatureType type) {
        return Integer.parseInt(get(type, "max_age"));
    }

    public int decreaseHealthPerMove(CreatureType type) {
        return Integer.parseInt(get(type, "decrease_health_per_move"));
    }

    private <T> String get(T inputType, String key) {
        String group = inputType.getClass().getSimpleName().toLowerCase().replace("type", "");
        String value = properties.getProperty(group + "." + inputType.toString().toLowerCase() + "." + key);
        if (value == null) {
            value = properties.getProperty(group + ".default." + key);
            if (value == null) {
                throw new RuntimeException(String.format("key %s.default.%s doesn't found in properties", group, key));
            }
        }
        return value;
    }

}


