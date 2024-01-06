package org.yt.jr.projects;

import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.maps.WorldMap;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;
import org.yt.jr.projects.utils.logs.Logger;

import java.io.IOException;

public class World {

    public static void main(String[] args) {
        Logger.initLogger(Config.CONFIG.getLogFilePath());

        WorldMap worldMap = WorldMap.getMap();
        LifeCycle plantsLifeCycle = new LifeCycle(CreaturesTypes.PLANTS);

        Logger.closeLogger();
    }

}
