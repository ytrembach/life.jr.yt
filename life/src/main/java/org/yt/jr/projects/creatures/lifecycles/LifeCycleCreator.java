package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.CreatureList;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.maps.WorldMap;
import org.yt.jr.projects.maps.WorldMapIterator;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;

import java.util.Iterator;

public class LifeCycleCreator {
    public static LifeCycle createLifeCycle(final CreaturesTypes type) {
        final LifeCycle lifeCycle = new LifeCycle(type);

        final Iterator<Location> mapIterator = WorldMap.getMap().iterator();
        while (mapIterator.hasNext()) {
            final Location location = mapIterator.next();
            initLocation(lifeCycle,location, type);
        }

        return lifeCycle;
    }

    private static void initLocation(final LifeCycle lifeCycle, final Location location, final CreaturesTypes type) {
        final CreatureList habitants = location.getHabitants();
        final double maxCreaturePerLocation = Config.CONFIG.getMaxCreaturePerLocation(type);
    }

}
