package org.yt.jr.projects.creatures.actions.move;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.maps.Location;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.Optional;

import static org.yt.jr.projects.maps.Location.less;
import static org.yt.jr.projects.maps.Location.more;

public class MoveAction implements Action {

    final private Creature creature;
    final private Location origin;

    public MoveAction(Creature creature) {
        this.creature = creature;
        this.origin = creature.getLocation();
    }

    @Override
    public void doAction(Creature party) {
        final Optional<Location> found = LocationSelector.getRandomDest(origin,
                Config.getConfig().maxStepsPerMove(creature.getType()));

        if (found.isPresent()) {
            final Location destination = found.get();
            synchronized (creature) {
                synchronized (less(origin, destination)) {
                    synchronized (more(origin, destination)) {
                        origin.removeCreature(creature);
                        destination.addCreature(creature);
                    }
                }
            }
            Logger.Log(LogSources.ACTION, LogLevels.INFO,
                    String.format("%s successfully moved from %s to %s", creature, origin, destination));
        } else {
            Logger.Log(LogSources.ACTION, LogLevels.ERROR,
                    String.format("%s failed to find location to move from %s in moveAction", creature, origin));
        }
    }
}