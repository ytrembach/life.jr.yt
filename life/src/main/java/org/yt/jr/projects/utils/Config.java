package org.yt.jr.projects.utils;

import java.nio.file.Path;
import java.util.Map;

public class Config {

    final public static Config CONFIG = new Config();

    public String getLogFilePath() {
        return "/home/yt/IdeaProjects/tmp/life.jr.log";
    }

    public int getPoolSize(CreaturesTypes type) {
        final Map<CreaturesTypes, Integer> sizes = Map.of(CreaturesTypes.PLANTS, 4);

        return sizes.get(type);
    }

    public long getTurnDurationInSecond(CreaturesTypes type) {
        final Map<CreaturesTypes, Long> durations = Map.of(CreaturesTypes.ALL, 1L);
        return durations.get(type);
    }

    public Double getReproduceProbability(CreaturesTypes type) {
        final Map<CreaturesTypes, Double> probabilities = Map.of(CreaturesTypes.PLANTS, 0.1);

        return probabilities.get(type);
    }

    public int getMapWidth() {
        return 3;
    }

    public int getMapHeight() {
        return 3;
    }
}


