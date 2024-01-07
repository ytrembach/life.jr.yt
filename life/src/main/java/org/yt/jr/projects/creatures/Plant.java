package org.yt.jr.projects.creatures;

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
    public void reproduce() {
        if (!location.isSpaceAvailable(type)) {
            Logger.Log(LogSources.CREATURE,LogLevels.DEBUG,
                    String.format("No slots for %s on %s",type,location));
            return;
        }
        double probability = Config.CONFIG.getReproduceProbability(type);
        double random = ThreadLocalRandom.current().nextDouble();
        if (random < probability) {
            bornChild();
        }
    }
}
