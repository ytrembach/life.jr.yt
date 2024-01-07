package org.yt.jr.projects.creatures;

import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.CreaturesTypes;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Creature {

    public Animal(CreaturesTypes type, int health) {
        super(type, health);
    }

    public abstract void move();

    public abstract void eat();

    @Override
    public void reproduce() {
        if (!location.isSpaceAvailable(type)) {
            Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                    String.format("No slots for %s on %s", type, location));
            return;
        }

        if (turnsToReproduce > 0) {
            synchronized (this) {
                Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("%s could reproduce after %d turns",
                        this, turnsToReproduce));
                turnsToReproduce--;
            }
            return;
        }

        double probability = Config.CONFIG.getReproduceProbability(type);
        double random = ThreadLocalRandom.current().nextDouble();
        if (random < probability) {
            Optional<Animal> readyToReproduce = findReadyToReproduce();
            if (readyToReproduce.isPresent()) {
                synchronized (this) {
                    Animal pair = readyToReproduce.get();
                    if (pair.turnsToReproduce == 0) {
                        synchronized (pair) {
                            bornChild();
                            pair.resetTurnsToReproduce();
                        }
                    }
                    resetTurnsToReproduce();
                }
            }
        }
    }

    public Optional<Animal> findReadyToReproduce() {
        return location.getHabitants().stream()
                .filter(creature -> creature.getType().equals(type))
                .filter(creature -> creature.getTurnsToReproduce() == 0)
                .map(creature -> (Animal) creature)
                .findAny();
    }
}
