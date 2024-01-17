package org.yt.jr.projects;

import org.yt.jr.projects.creatures.actions.eat.FoodMatrix;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleCreator;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleType;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.maps.WorldMap;
import org.yt.jr.projects.maps.WorldMapIterator;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.StreamSupport;

public class World {

    final static private Map<LifeCycleType, LifeCycle> lifeCycles = new HashMap<>();

    public static void main(String... args) {
        try {
            if (args.length > 0) {
                Config.initConfig(args[0]);
            } else {
                Config.initConfig();
            }
        } catch (Exception e) {
            usage();
            return;
        }
        Logger.initLogger(Config.getConfig().logFile());

        WorldMap worldMap = WorldMap.getMap();
        FoodMatrix.FOOD_MATRIX.initFoodMatrix();

        startLifeCycles(LifeCycleType.values());

        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            String userInput;

            prompt();
            userInput = console.readLine();
            while (!"EXIT".equalsIgnoreCase(userInput)) {
                if ("".equals(userInput)) {
                    showLifeCycles();
                } else {
                    showLocation(userInput);
                }

                prompt();
                userInput = console.readLine();
            }
        } catch (
                Exception e) {
            Logger.Log(LogSources.SYSTEM, LogLevels.FATAL, String.format("Exception %s, message: %s", e, e.getMessage()));
            for (StackTraceElement el : e.getStackTrace()) {
                Logger.Log(LogSources.SYSTEM, LogLevels.FATAL, el.toString());
            }
        }

        stopLifeCycles();
        Logger.closeLogger();
    }

    private static void startLifeCycles(LifeCycleType... types) {
        for (LifeCycleType type : types) {
            final LifeCycle lifeCycle = LifeCycleCreator.createLifeCycle(type);
            lifeCycles.put(type, lifeCycle);
        }
    }

    private static void stopLifeCycles(LifeCycleType... types) {
        for (LifeCycle lifeCycle : lifeCycles.values()) {
            lifeCycle.close();
        }
    }

    private static void showLifeCycles() {
        for (LifeCycle lifeCycle : lifeCycles.values()) {
            lifeCycle.showStatus();
        }
    }

    private static void showLocation(final String input) {

        final String[] items = input.trim().split("\s+");
        if (items.length != 2) {
            prompt();
            return;
        }

        final String first = items[0];
        final String second = items[1];
        try {
            if ("L".equalsIgnoreCase(first)) {
                final int locationId = Integer.parseInt(second);
                Optional<Location> foundLocation = StreamSupport.stream(WorldMap.getMap().spliterator(), false)
                        .filter(loc -> loc.getId() == locationId)
                        .findAny();
                if (foundLocation.isPresent()) {
                    final Location loc = foundLocation.get();
                    System.out.println(loc);
                    List.copyOf(loc.getHabitants()).forEach(System.out::println);
                }

            } else {
                int row = Integer.parseInt(first);
                int col = Integer.parseInt(second);

                if (row >= 0 && row < Config.getConfig().mapRows()
                        && col >= 0 && col < Config.getConfig().mapCols()) {
                    System.out.println(WorldMap.getMap().getLocation(row, col));
                } else {
                    System.out.printf("Location with row %d and col %s is outside the map", row, col);
                }
            }
        } catch (NumberFormatException e) {
        }
    }

    private static void prompt() {
        System.out.print("Enter space separated Row and Col pair to check Location, press Enter to get LifeCycles stats or type EXIT to exit: ");
    }

    private static void usage() {
        System.out.printf("The parameter should be path to the properties config file %s\n", Config.PROPERTIES_FILENAME);
    }
}
