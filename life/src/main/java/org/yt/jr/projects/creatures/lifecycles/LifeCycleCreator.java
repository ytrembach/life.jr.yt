package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.maps.WorldMap;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.creatures.CreatureType;

import java.util.concurrent.ThreadLocalRandom;

public class LifeCycleCreator {
    public static LifeCycle createLifeCycle(final LifeCycleType type) {
        final LifeCycle lifeCycle = new LifeCycle(type);

        for (Location location : WorldMap.getMap()) {
            for (CreatureType creatureType : lifeCycle.getType().getTypes()) {
                initLocation(lifeCycle, location, creatureType);
                LifeCycle.LIFECYCLES.put(creatureType, lifeCycle);
            }
        }
        return lifeCycle;
    }

    private static void initLocation(final LifeCycle lifeCycle, final Location location, final CreatureType type) {
        final int maxCreatures = Config.CONFIG.maxCreaturePerLocation(type);
        final double initShare = Config.CONFIG.initCreaturePerLocationShare(type);

        final long initCreatureCount =
                Math.round(maxCreatures * initShare * ThreadLocalRandom.current().nextDouble());

        for (int c = 0; c < initCreatureCount; c++) {
            final Creature creature = CreatureFactory.CREATURE_FACTORY.getCreature(type)
                    .apply(ThreadLocalRandom.current().nextInt(1,Config.CONFIG.creatureDefaultHealth("max")));
            synchronized (creature) {
                synchronized (location) {
                    location.addCreature(creature);
                }
                synchronized (lifeCycle) {
                    lifeCycle.addCreature(creature);
                }
            }
        }
    }

}
