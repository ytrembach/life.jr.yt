package org.yt.jr.projects.creatures.actions.reproduce;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.utils.logs.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class BornCheck implements Check {

    final private Creature firstParent;
    final private CreatureType childType;

    public BornCheck(final Creature firstParent) {
        this.firstParent = firstParent;
        this.childType = firstParent.getType();
    }

    @Override
    public Optional<Creature> checkPartyToAct() {
        if (firstParent.isReadyToReproduce(false)) {
            double probability = Config.CONFIG.reproduceProbability(childType);
            if (ThreadLocalRandom.current().nextDouble() < probability) {
                Optional<Creature> found = findPairToReproduce(firstParent);
                if (found.isPresent()) {
                    return found;
                } else {
                    Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("pair not found for %s", firstParent));
                }
            } else {
                Logger.Log(LogSources.CREATURE, LogLevels.DEBUG, String.format("probability failed for %s", firstParent));
            }
        }
        return Optional.empty();
    }

    public static Optional<Creature> findPairToReproduce(final Creature parent) {
        final CreatureType type = parent.getType();
        return parent.getLocation().getHabitants().stream()
                .filter(creature -> creature.getType().equals(type))
                .filter(creature -> creature.isReadyToReproduce(false))
                .filter(creature -> parent != creature)
                .findAny();
    }
}
