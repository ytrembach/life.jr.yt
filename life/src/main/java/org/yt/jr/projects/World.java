package org.yt.jr.projects;

import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleCreator;
import org.yt.jr.projects.creatures.lifecycles.LifeCycleType;
import org.yt.jr.projects.maps.WorldMap;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class World {

    public static void main(String[] args) {
        Logger.initLogger(Config.CONFIG.logFile());

        WorldMap worldMap = WorldMap.getMap();

        try (
                LifeCycle plantsLifeCycle = LifeCycleCreator.createLifeCycle(LifeCycleType.PLANTS);
                LifeCycle herbivoresCycle = LifeCycleCreator.createLifeCycle(LifeCycleType.HERBIVORES)
        ) {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            prompt();
            userInput = console.readLine();
            while (!"EXIT".equalsIgnoreCase(userInput)) {
                if ("".equals(userInput)) {
                    plantsLifeCycle.showStatus();
                    herbivoresCycle.showStatus();
                }

                String[] items = userInput.split("\s+");
                if (items.length == 2) {
                    try {
                        int row = Integer.parseInt(items[0]);
                        int col = Integer.parseInt(items[1]);

                        if (row >= 0 && row < Config.CONFIG.mapHeight()
                                && col >= 0 && col < Config.CONFIG.mapWidth()) {
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
            Logger.Log(LogSources.SYSTEM, LogLevels.FATAL, String.format("Exception %s, message: %s",e,e.getMessage()) );
            for (StackTraceElement el : e.getStackTrace()) {
                Logger.Log(LogSources.SYSTEM, LogLevels.FATAL, el.toString());
            }
        }
        Logger.closeLogger();

    }

    public static void prompt() {
        System.out.print("Enter space separated Row and Col pair to check Location, press Enter to get LifeCycles stats or type EXIT to exit: ");
    }
}
