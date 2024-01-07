package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.maps.WorldMap;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;

public class LifeCycleCreator {
    public static LifeCycle createLifeCycle(final LifeCycleTypes type) {
        final LifeCycle lifeCycle = new LifeCycle(type);

        for (Location location : WorldMap.getMap()) {
            for (CreaturesTypes creaturesType : lifeCycle.getType().getTypes()) {
                initLocation(lifeCycle, location, creaturesType);
                LifeCycle.LIFECYCLES.put(creaturesType, lifeCycle);
            }
        }
        return lifeCycle;
    }

    private static void initLocation(final LifeCycle lifeCycle, final Location location, final CreaturesTypes type) {
        final int maxCreatures = Config.CONFIG.getMaxCreaturePerLocation(type);
        final double initShare = Config.CONFIG.getInitCreaturePerLocationShare(type);

        final long initCreatureCount = Math.round(maxCreatures * initShare);

        for (int c = 0; c < initCreatureCount; c++) {
            final Creature creature = CreatureFactory.CREATURE_FACTORY.getCreature(type).apply(Creature.MAX_HEALTH);
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
