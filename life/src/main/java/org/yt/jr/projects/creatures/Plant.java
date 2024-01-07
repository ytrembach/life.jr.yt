package org.yt.jr.projects.creatures;

import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

public class Plant extends Creature {
    @Override
    public void run() {
        Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,String.format("plant running"));
    }

    @Override
    void reproduce() {
        Double probability = Config.CONFIG.getReproduceProbability(CreaturesTypes.PLANTS);
    }
}
