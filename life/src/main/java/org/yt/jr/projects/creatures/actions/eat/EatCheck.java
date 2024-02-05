package org.yt.jr.projects.creatures.actions.eat;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class EatCheck implements Check {

    final private Creature eater;

    public EatCheck(final Creature eater) {
        this.eater = eater;
    }

    @Override
    public Optional<Creature> checkReadyToAct() {
        if (eater.getHealth() < Config.getConfig().creatureDefaultHealthMax(eater.getType()) / 2) {
            final Optional<Creature> foodFound = searchFood();
            if (foodFound.isPresent()) {
                final Creature food = foodFound.get();
                final double probability = FoodMatrix.FOOD_MATRIX.getProbability(eater.getType(), food.getType());
                if (ThreadLocalRandom.current().nextDouble() < probability) {
                    return foodFound;
                } else {
                    Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                            String.format("%s found food %s, but lost it", eater, food));
                }
            } else {
                Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                        String.format("%s didn't found food in %s", eater, eater.getLocation()));
            }
        }
        return Optional.empty();
    }

    public Optional<Creature> searchFood() {
        if (eater.getLocation().getHabitants() == null) {
            return Optional.empty();
        }
        return List.copyOf(eater.getLocation().getHabitants()).stream()
                .filter(foodCandidate -> foodCandidate != eater)
                .filter(foodCandidate -> FoodMatrix.FOOD_MATRIX.getProbability(eater.getType(), foodCandidate.getType()) > 0)
                .max(new FoodComparator(eater));
    }
}
