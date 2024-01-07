package org.yt.jr.projects.utils;

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

    public int getPoolSize(CreaturesTypes type) {
        final Map<CreaturesTypes, Integer> poolSizes = Map.of(
                CreaturesTypes.ALL, 4
        );

        return poolSizes.getOrDefault(type, poolSizes.get(CreaturesTypes.ALL));
    }

    public long getTurnDurationInSecond(CreaturesTypes type) {
        final Map<CreaturesTypes, Long> durations = Map.of(
                CreaturesTypes.ALL, 1L
        );
        return durations.getOrDefault(type, durations.get(CreaturesTypes.ALL) );
    }

    public int getMaxCreaturePerLocation(CreaturesTypes type) {
        final Map<CreaturesTypes, Integer> maxNumbers = Map.of(
                CreaturesTypes.ALL, 10,
                CreaturesTypes.PLANTS, 200
        );

        return maxNumbers.getOrDefault(type, maxNumbers.get(CreaturesTypes.ALL));
    }

    public Double getReproduceProbability(CreaturesTypes type) {
        final Map<CreaturesTypes, Double> probabilities = Map.of(
                CreaturesTypes.ALL, 0.1
        );

        return probabilities.getOrDefault(type, probabilities.get(CreaturesTypes.ALL));
    }

    public int getMapWidth() {
        return 3;
    }

    public int getMapHeight() {
        return 3;
    }
}


