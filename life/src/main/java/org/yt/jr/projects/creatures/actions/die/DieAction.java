package org.yt.jr.projects.creatures.actions.die;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.creatures.lifecycles.LifeCycle;
import org.yt.jr.projects.maps.Location;

public class DieAction extends Action {
    final private Creature creature;
    final private Location location;
    final LifeCycle lifeCycle;

    public DieAction(final Creature creature) {
        this.creature = creature;
        this.location = creature.getLocation();
        this.lifeCycle = LifeCycle.LIFECYCLES.get(creature.getType());
    }

    @Override
    public boolean doAction() {
        synchronized (creature) {
            synchronized (location) {
                location.removeCreature(creature);
            }
            synchronized (lifeCycle) {
                lifeCycle.removeCreature(creature);
            }
        }
        return true;
    }
}
