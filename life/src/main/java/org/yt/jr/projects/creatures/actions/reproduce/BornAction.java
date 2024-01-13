package org.yt.jr.projects.creatures.actions.reproduce;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.creatures.lifecycles.CreatureFactory;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.utils.Config;

import static org.yt.jr.projects.creatures.Creature.*;

public class BornAction extends Action {
    final private Creature firstParent;
    final private Creature secondParent;
    final private Location location;
    final private CreatureType childType;
    final private LifeCycle lifeCycle;

    public BornAction(final Creature firstParent, final Creature secondParent) {
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.childType = parentsHasSameType(firstParent, secondParent) ? firstParent.getType() : CreatureType.NONEXISTENT;
        this.location = parentsInSameLocation(firstParent, secondParent) ? firstParent.getLocation() : Location.NOWHERE;
        this.lifeCycle = LifeCycle.LIFECYCLES.get(childType);
    }

    @Override
    public boolean doAction() {
        final Creature child;
        synchronized (less(firstParent, secondParent)) {
            synchronized (more(firstParent, secondParent)) {
                synchronized (location) {
                    if (!firstParent.getLocation().equals(secondParent.getLocation()) ||
                            !firstParent.isReadyToReproduce(true) ||
                            !secondParent.isReadyToReproduce(true)) {
                        return false;
                    }
                    firstParent.resetTurnsToReproduce();
                    secondParent.resetTurnsToReproduce();
                    child = CreatureFactory.CREATURE_FACTORY.getCreature(childType).apply(Config.CONFIG.creatureDefaultHealth("child"));
                    synchronized (child) {
                        location.addCreature(child);
                        synchronized (lifeCycle) {
                            lifeCycle.addCreature(child);
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean parentsHasSameType(Creature firstParent, Creature secondParent) {
        return firstParent.getType().equals(secondParent.getType());
    }

    private boolean parentsInSameLocation(Creature firstParent, Creature secondParent) {
        return firstParent.getLocation().equals(secondParent.getLocation());
    }
}