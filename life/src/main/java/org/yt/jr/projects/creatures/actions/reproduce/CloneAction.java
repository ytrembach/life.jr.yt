package org.yt.jr.projects.creatures.actions.reproduce;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.creatures.lifecycles.CreatureFactory;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

public class CloneAction implements Action {
    final private Creature parent;
    final private Location location;
    final private CreatureType childType;
    final private LifeCycle lifeCycle;

    public CloneAction(final Creature parent) {
        this.parent = parent;
        this.location = parent.getLocation();
        this.childType = parent.getType();
        this.lifeCycle = LifeCycle.LIFECYCLES.get(childType);
    }

    @Override
    public void doAction(final Creature notUsed) {
        final Creature child;
        boolean result = false;

        synchronized (parent) {
            synchronized (location) {
                if (parent.isReadyToReproduce(true)) {
                    parent.resetTurnsToReproduce();
                    child = CreatureFactory.CREATURE_FACTORY.getCreature(childType)
                            .apply(Config.getConfig().creatureDefaultHealthChild(childType));
                    synchronized (child) {
                        location.addCreature(child);
                        synchronized (lifeCycle) {
                            lifeCycle.addCreature(child);
                        }
                        result = true;
                    }
                }
            }
        }
        if (result) {
            Logger.Log(LogSources.ACTION, LogLevels.INFO,
                    String.format("%s successfully cloned", parent));
        } else {
            Logger.Log(LogSources.ACTION, LogLevels.ERROR,
                    String.format("%s failed to clone in cloneAction", parent));
        }
    }
}