package org.yt.jr.projects;

import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleCreator;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleTypes;
import org.yt.jr.projects.maps.WorldMap;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class World {

    public static void main(String[] args) {
        Logger.initLogger(Config.CONFIG.getLogFilePath());

        WorldMap worldMap = WorldMap.getMap();

        try (
                LifeCycle plantsLifeCycle = LifeCycleCreator.createLifeCycle(LifeCycleTypes.PLANTS);
                LifeCycle herbivoresCycle = LifeCycleCreator.createLifeCycle(LifeCycleTypes.HERBIVORES);
        ) {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            prompt();
            userInput = console.readLine();
            while (!"EXIT".equalsIgnoreCase(userInput)) {
                String[] items = userInput.split("\s+");
                if (items.length == 2) {
                    try {
                        int row = Integer.parseInt(items[0]);
                        int col = Integer.parseInt(items[1]);

                        if (row >= 0 && row < Config.CONFIG.getMapHeight()
                                && col >= 0 && col < Config.CONFIG.getMapWidth()) {
                            System.out.println(worldMap.getLocation(row, col));
                        } else {
                            System.out.printf("Location with row %d and col %s is outside the map", row, col);
                        }
                    } catch (NumberFormatException e) {

                    }
                }
                prompt();
                userInput = console.readLine();
            }
        } catch (Exception e) {
            Logger.Log(LogSources.SYSTEM, LogLevels.FATAL, e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Logger.Log(LogSources.SYSTEM, LogLevels.FATAL, el.toString());
            }
        }
        Logger.closeLogger();

    }

    public static void prompt() {
        System.out.print("Enter row and col pair separated by space to check location: ");
    }
}
