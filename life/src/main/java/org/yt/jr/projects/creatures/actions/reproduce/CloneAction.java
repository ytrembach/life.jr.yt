package org.yt.jr.projects.creatures.actions.reproduce;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.creatures.lifecycles.CreatureFactory;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.creatures.CreaturesTypes;
import org.yt.jr.projects.utils.Config;

public class CloneAction extends Action {
    final private Creature parent;
    final private Location location;
    final private CreaturesTypes childType;
    final private LifeCycle lifeCycle;

    public CloneAction(final Creature parent) {
        this.parent = parent;
        this.location = parent.getLocation();
        this.childType = parent.getType();
        this.lifeCycle = LifeCycle.LIFECYCLES.get(childType);
    }

    @Override
    public boolean doAction() {
        final Creature child;

        synchronized (parent) {
            synchronized (location) {
                if (!parent.isReadyToReproduce(true)) {
                    return false;
                }
                parent.resetTurnsToReproduce();
                child = CreatureFactory.CREATURE_FACTORY.getCreature(childType).apply(Config.CREATURE_CHILD_HEALTH);
                synchronized (child) {
                    location.addCreature(child);
                    synchronized (lifeCycle) {
                        lifeCycle.addCreature(child);
                    }
                }
            }
        }
        return true;
    }
}