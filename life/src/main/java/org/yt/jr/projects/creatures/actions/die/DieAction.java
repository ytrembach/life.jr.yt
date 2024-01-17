package org.yt.jr.projects.creatures.actions.die;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

public class DieAction implements Action {
    final private Creature creature;
    final private Location location;
    final LifeCycle lifeCycle;

    public DieAction(final Creature creature) {
        this.creature = creature;
        this.location = creature.getLocation();
        this.lifeCycle = LifeCycle.LIFECYCLES.get(creature.getType());
    }

    @Override
    public void doAction(final Creature notUsed) {
        boolean result = false;
        String creatureString = creature.toString();
        synchronized (creature) {
            if (creature.isAlive()) {
                synchronized (location) {
                    location.removeCreature(creature);
                    synchronized (lifeCycle) {
                        lifeCycle.removeCreature(creature);
                    }
                    creature.setHealth(-1);
                    result = true;
                }
            }
        }
        if (result) {
            Logger.Log(LogSources.ACTION, LogLevels.INFO,
                    String.format("%s successfully died", creatureString));
        } else {
            Logger.Log(LogSources.ACTION, LogLevels.ERROR,
                    String.format("%s already dead now", creatureString));
        }
    }
}