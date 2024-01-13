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

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.yt.jr.projects.creatures.Creature.*;

public class BornAction implements Action {
    final private Creature firstParent;
    private Creature secondParent;
    final private Location location;
    final private CreatureType childType;
    final private LifeCycle lifeCycle;

    public BornAction(final Creature parent) {
        this.firstParent = parent;
        this.childType = parent.getType();
        this.location = parent.getLocation();
        this.lifeCycle = LifeCycle.LIFECYCLES.get(childType);
    }

    @Override
    public boolean checkReady() {
        if (firstParent.isReadyToReproduce(false)) {
            double probability = Config.CONFIG.reproduceProbability(childType);
            if (ThreadLocalRandom.current().nextDouble() < probability) {
                Optional<Creature> found = findPairToReproduce();
                if (found.isPresent()) {
                    secondParent = found.get();
                    return true;
                } else {
                    Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("pair not found for %s", firstParent));
                }
            } else {
                Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("probability failed for %s", firstParent));
            }
        }
        return false;
    }
    @Override
    public void doAction() {
        final Creature child;
        boolean result = false;

        synchronized (less(firstParent, secondParent)) {
            synchronized (more(firstParent, secondParent)) {
                synchronized (location) {
                    if (secondParent != null &&
                            firstParent.getLocation().equals(secondParent.getLocation()) &&
                            firstParent.isReadyToReproduce(true) &&
                            secondParent.isReadyToReproduce(true)) {
                        firstParent.resetTurnsToReproduce();
                        secondParent.resetTurnsToReproduce();
                        child = CreatureFactory.CREATURE_FACTORY.getCreature(childType).apply(Config.CONFIG.creatureDefaultHealth("child"));
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
            Logger.Log(LogSources.CREATURE, LogLevels.INFO, String.format("%s successfully paired with %s", firstParent, secondParent));
        } else {
            Logger.Log(LogSources.CREATURE, LogLevels.ERROR, String.format("%s failed to pair in bordAction", firstParent));
        }
    }

    private Optional<Creature> findPairToReproduce() {
        return location.getHabitants().stream()
                .filter(creature -> creature.getType().equals(childType))
                .filter(creature -> creature.isReadyToReproduce(false))
                .filter(creature -> firstParent != creature)
                .findAny();
    }
}