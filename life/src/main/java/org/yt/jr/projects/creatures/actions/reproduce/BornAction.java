package org.yt.jr.projects.creatures.actions.reproduce;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.lifecycles.CreatureFactory;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.creatures.CreaturesTypes;
import org.yt.jr.projects.utils.Config;

import static org.yt.jr.projects.creatures.Creature.*;

public class BornAction {
    final private Creature firstParent;
    final private Creature secondParent;
    final private Location location;
    final private CreaturesTypes childType;
    final private LifeCycle lifeCycle;

    public BornAction(final Creature firstParent, final Creature secondParent) {
        this.firstParent = firstParent;
        this.secondParent = secondParent;
        this.childType = parentsHasSameType(firstParent, secondParent) ? firstParent.getType() : CreaturesTypes.NONEXISTENT;
        this.location = parentsInSameLocation(firstParent, secondParent) ? firstParent.getLocation() : Location.NOWHERE;
        this.lifeCycle = LifeCycle.LIFECYCLES.get(childType);
    }

    public void bornChild() {
        final Creature child;
        synchronized (less(firstParent, secondParent)) {
            synchronized (more(firstParent, secondParent)) {
                synchronized (location) {
                    if (!firstParent.getLocation().equals(secondParent.getLocation()) ||
                            !firstParent.isReadyToReproduce(true) ||
                            !secondParent.isReadyToReproduce(true)) {
                        return;
                    }
                    firstParent.resetTurnsToReproduce();
                    secondParent.resetTurnsToReproduce();
                    child = CreatureFactory.CREATURE_FACTORY.getCreature(childType).apply(Config.CREATURE_CHILD_HEALTH);
                    synchronized (child) {
                        location.addCreature(child);
                        synchronized (lifeCycle) {
                            lifeCycle.addCreature(child);
                        }
                    }
                }
            }
        }
    }

    private boolean parentsHasSameType(Creature firstParent, Creature secondParent) {
        return firstParent.getType().equals(secondParent.getType());
    }

    private boolean parentsInSameLocation(Creature firstParent, Creature secondParent) {
        return firstParent.getLocation().equals(secondParent.getLocation());
    }
}