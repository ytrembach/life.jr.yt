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

import static org.yt.jr.projects.creatures.Creature.*;

public class BornAction implements Action {
    final private Creature firstParent;
    final private CreatureType childType;
    final private Location location;
    final private LifeCycle lifeCycle;

    public BornAction(final Creature parent) {
        this.firstParent = parent;
        this.childType = parent.getType();
        this.location = parent.getLocation();
        this.lifeCycle = LifeCycle.LIFECYCLES.get(childType);
    }

    @Override
    public void doAction(final Creature party) {
        final Creature child;
        boolean result = false;

        synchronized (less(firstParent, party)) {
            synchronized (more(firstParent, party)) {
                synchronized (location) {
                    if (firstParent.getLocation().equals(party.getLocation())
                            && firstParent.isReadyToReproduce(true)
                            && party.isReadyToReproduce(true)) {

                        firstParent.resetTurnsToReproduce();
                        party.resetTurnsToReproduce();
                        child = CreatureFactory.CREATURE_FACTORY.getCreature(childType).apply(Config.getConfig().creatureDefaultHealthChild(childType));
                        synchronized (child) {
                            location.addCreature(child);
                            synchronized (lifeCycle) {
                                lifeCycle.addCreature(child);
                            }
                        }
                        result = true;

                    }
                }
            }
        }
        if (result) {
            Logger.Log(LogSources.ACTION, LogLevels.INFO,
                    String.format("%s successfully paired with %s", firstParent, party));
        } else {
            Logger.Log(LogSources.ACTION, LogLevels.ERROR,
                    String.format("%s failed to pair with %sin bornAction", firstParent, party));
        }
    }
}