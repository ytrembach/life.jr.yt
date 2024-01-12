package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.reproduce.BornAction;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;


import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Creature {
    public Animal(CreaturesTypes type, int health) {
        super(type, health);
    }

    @Override
    public void tryToReproduce() {
        if (isReadyToReproduce(false)) {
            Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("trying to reproduce %s in %s", this, location));
            double probability = Config.CONFIG.getReproduceProbability(type);
            if (ThreadLocalRandom.current().nextDouble() < probability) {
                Optional<Animal> found = findPairToReproduce();
                if (found.isPresent()) {
                    Animal pair = found.get();
                    new BornAction(this, pair).bornChild();
                    Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                            String.format("%s successfully paired with %s", this, pair));
                } else {
                    Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("pair not found for %s", this));
                }
            } else {
                Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("probability failed for %s", this));
            }
        }
    }

    private Optional<Animal> findPairToReproduce() {
        return location.getHabitants().stream()
                .filter(creature -> creature.getType().equals(type))
                .filter(creature -> creature.isReadyToReproduce(false))
                .map(creature -> (Animal) creature)
                .filter(creature -> this != creature)
                .findAny();
    }
}
