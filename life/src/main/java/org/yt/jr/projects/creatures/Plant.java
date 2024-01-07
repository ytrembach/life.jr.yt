package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.lifecycles.CreatureFactory;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;
import java.util.concurrent.ThreadLocalRandom;

public class Plant extends Creature {
    public Plant(int health) {
        super(CreaturesTypes.PLANTS, health);
    }

    @Override
    void reproduce() {
        Double probability = Config.CONFIG.getReproduceProbability(CreaturesTypes.PLANTS);
        Double random = ThreadLocalRandom.current().nextDouble();
        Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                String.format("trying to reproduce %s (%f / %f)", this, random, probability));
        if (random < probability) {
            final Creature creature = CreatureFactory.CREATURE_FACTORY.getCreature(type).apply(CHILD_HEALTH);
            location.addCreature(creature);
            LifeCycle.LIFECYCLES.get(type).addCreature(creature);
        }
    }
}
