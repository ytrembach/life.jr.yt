package org.yt.jr.projects.creatures;

import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;

public class Plant extends Creature {
    @Override
    public void run() {

    }

    @Override
    void reproduce() {
        Double probability = Config.CONFIG.getReproduceProbability(CreaturesTypes.PLANTS);
    }
}
