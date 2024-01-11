package org.yt.jr.projects.utils;

import org.yt.jr.projects.creatures.CreaturesTypes;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleTypes;
import org.yt.jr.projects.utils.logs.LogLevels;

import java.util.Map;

public class Config {

    final public static Config CONFIG = new Config();

    final public static int CREATURE_MAX_HEALTH = 100;
    final public static int CREATURE_CHILD_HEALTH = 10;

    public String getLogFilePath() {
        return "/home/yt/IdeaProjects/tmp/life.jr.log";
    }

    public LogLevels getLogLevel() {
        return LogLevels.DEBUG;
    }

    public int getPoolSize(LifeCycleTypes type) {
        int valueDefault = 4;
        final Map<LifeCycleTypes, Integer> poolSizes = Map.of(

        );
        return poolSizes.getOrDefault(type, valueDefault);
    }

    public int getMaxCreaturePerLocation(CreaturesTypes type) {
        int valueDefault = 10;
        final Map<CreaturesTypes, Integer> maxNumbers = Map.of(
                CreaturesTypes.PLANTS, 200
        );
        return maxNumbers.getOrDefault(type, valueDefault);
    }

    public Double getInitCreaturePerLocationShare(CreaturesTypes type) {
        double valueDefault = 0.5;
        final Map<CreaturesTypes, Double> shares = Map.of(
                CreaturesTypes.PLANTS, 0.1
        );
        return shares.getOrDefault(type, valueDefault);
    }

    public int getTurnsPerMove(LifeCycleTypes type) {
        int valueDefault = 1;
        final Map<LifeCycleTypes, Integer> durations = Map.of(
                LifeCycleTypes.PLANTS, 5,
                LifeCycleTypes.HERBIVORES, 3,
                LifeCycleTypes.CARNIVORES, 1
        );
        return durations.getOrDefault(type, valueDefault);
    }

    public int getTurnsToReproduceDefault(CreaturesTypes type) {
        int valueDefault = 1;
        final Map<CreaturesTypes, Integer> probabilities = Map.of(
                CreaturesTypes.HORSE, 5,
                CreaturesTypes.DEER, 5
        );
        return probabilities.getOrDefault(type, valueDefault);
    }

    public Double getReproduceProbability(CreaturesTypes type) {
        double valueDefault = 0.1;
        final Map<CreaturesTypes, Double> probabilities = Map.of(
                CreaturesTypes.PLANTS, 0.2,
                CreaturesTypes.HORSE, 0.15,
                CreaturesTypes.DEER, 0.10
        );
        return probabilities.getOrDefault(type, valueDefault);
    }

    public int getMaxAge(CreaturesTypes type) {
        int valueDefault = 1;
        final Map<CreaturesTypes, Integer> probabilities = Map.of(
                CreaturesTypes.PLANTS, 200,
                CreaturesTypes.HORSE, 80,
                CreaturesTypes.DEER, 100
        );
        return probabilities.getOrDefault(type, valueDefault);
    }

    public int getMapWidth() {
        return 3;
    }

    public int getMapHeight() {
        return 3;
    }
}


