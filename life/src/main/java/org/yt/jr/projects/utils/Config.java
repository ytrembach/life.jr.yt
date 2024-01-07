package org.yt.jr.projects.utils;

import org.yt.jr.projects.creatures.lifecycles.LifeCycleTypes;
import org.yt.jr.projects.utils.logs.LogLevels;

import java.util.Map;

public class Config {

    final public static Config CONFIG = new Config();

    public String getLogFilePath() {
        return "/home/yt/IdeaProjects/tmp/life.jr.log";
    }

    public LogLevels getLogLevel() {
        return  LogLevels.DEBUG;
    }

    public int getPoolSize(LifeCycleTypes type) {
        int valueDefault = 4;
        final Map<LifeCycleTypes, Integer> poolSizes = Map.of(
                LifeCycleTypes.PLANTS, 4
        );
        return poolSizes.getOrDefault(type, valueDefault);
    }

    public long getTurnDurationInSecond(LifeCycleTypes type) {
        long valueDefault = 1;
        final Map<LifeCycleTypes, Long> durations = Map.of(
                LifeCycleTypes.PLANTS, 1L
        );
        return durations.getOrDefault(type, valueDefault );
    }

    public int getMaxCreaturePerLocation(CreaturesTypes type) {
        int valueDefault = 10;
        final Map<CreaturesTypes, Integer> maxNumbers = Map.of(
                CreaturesTypes.PLANTS, 200
        );

        return maxNumbers.getOrDefault(type, valueDefault);
    }

    public Double getInitCreaturePerLocationShare(CreaturesTypes type) {
        double valueDefault = 0.1;
        final Map<CreaturesTypes, Double> shares = Map.of(
                CreaturesTypes.PLANTS, 0.2
        );

        return shares.getOrDefault(type, valueDefault);
    }

    public Double getReproduceProbability(CreaturesTypes type) {
        double valueDefault = 0.1;
        final Map<CreaturesTypes, Double> probabilities = Map.of(
                CreaturesTypes.PLANTS, 0.1
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


