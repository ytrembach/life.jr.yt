package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.CreatureList;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.maps.WorldMap;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;

public class LifeCycleCreator {
    public static LifeCycle createLifeCycle(final CreaturesTypes type) {
        final LifeCycle lifeCycle = new LifeCycle(type);

        for (Location location : WorldMap.getMap()) {
            initLocation(lifeCycle, location, type);
        }

        LifeCycle.LIFECYCLES.put(type, lifeCycle);
        return lifeCycle;
    }

    private static void initLocation(final LifeCycle lifeCycle, final Location location, final CreaturesTypes type) {
        final CreatureList habitants = location.getHabitants();
        final int maxCreaturePerLocation = Config.CONFIG.getMaxCreaturePerLocation(type);

        for (int c = 0; c < 5; c++) {
            final Creature creature = CreatureFactory.CREATURE_FACTORY.getCreature(type).apply(Creature.MAX_HEALTH);
            location.addCreature(creature);
            lifeCycle.addCreature(creature);
        }
    }

}
