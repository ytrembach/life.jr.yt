package org.yt.jr.projects.utils;

import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleType;
import org.yt.jr.projects.utils.logs.LogLevels;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    final public static String PROPERTIES_FILENAME = "life.properties";
    final private static String LOGFILE_NAME = "life.log";

    final private Properties properties;
    private static Config CONFIG;

    private Config(final String pathToConfig) throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(pathToConfig));

    }

    private Config() throws IOException {
        this(Thread.currentThread().getContextClassLoader().getResource("").getPath() + PROPERTIES_FILENAME);
    }

    public static void initConfig() throws IOException {
        if (CONFIG == null) {
            CONFIG = new Config();
        }
    }

    public static void initConfig(final String pathToConfig) throws IOException {
        if (CONFIG == null) {
            CONFIG = new Config(pathToConfig);
        }
    }

    public static Config getConfig() {
        return CONFIG;
    }

    // final public static Config CONFIG; = new Config(PROPERTIES_FILENAME);

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
    public int poolSize(final LifeCycleType type) {
        return Integer.parseInt(get(type, "pool.size"));
    }

    public int turnsPerMove(final LifeCycleType type) {

        return Integer.parseInt(get(type, "turns_per_move"));
    }

    // Creatures
    public int creatureDefaultHealthMax(final CreatureType type) {
        return Integer.parseInt(get(type, "health.max"));
    }

    public int creatureDefaultHealthChild(final CreatureType type) {
        return Integer.parseInt(get(type, "health.child"));
    }

    public int maxCreaturePerLocation(final CreatureType type) {
        return Integer.parseInt(get(type, "per_location.max"));
    }

    public float initCreaturePerLocationShare(final CreatureType type) {
        return Float.parseFloat(get(type, "per_location.init_max"));
    }

    public int turnsToReproduce(final CreatureType type) {

        return Integer.parseInt(get(type, "turns_per_reproduce"));
    }

    public float reproduceProbability(final CreatureType type) {
        return Float.parseFloat(get(type, "reproduce_probability"));
    }

    public int maxAge(final CreatureType type) {
        return Integer.parseInt(get(type, "max_age"));
    }

    public float ownWeight(final CreatureType type) {
        return Float.parseFloat(get(type, "own_weight"));
    }

    public float needFoodWeight(final CreatureType type) {
        return Float.parseFloat(get(type, "need_food_weight"));
    }

    public int eatAttemptsPerMove(final CreatureType type) {
        return Integer.parseInt(get(type, "max_eat_attempts_per_move"));
    }

    public int decreaseHealthPerMove(final CreatureType type) {
        return Integer.parseInt(get(type, "decrease_health_per_move"));
    }

    public int maxStepsPerMove(final CreatureType type) {
        return Integer.parseInt(get(type, "max_steps"));
    }

    // Food Matrix
    public float foodMatrixValue(final CreatureType eaterType, final CreatureType foodType) {
        final String prefix = "creature.food_matrix.";
        final String eaterTypeName = eaterType.name().toLowerCase();
        final String foodTypeName = foodType.name().toLowerCase();
        final String value = properties.getProperty(prefix + eaterTypeName + "." + foodTypeName);
        return value != null ? Float.parseFloat(value) : 0;
    }

    // get
    private <T> String get(final T inputType, final String key) {
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


