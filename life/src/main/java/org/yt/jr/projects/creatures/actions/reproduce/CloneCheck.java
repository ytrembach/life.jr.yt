package org.yt.jr.projects.creatures.actions.reproduce;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class CloneCheck implements Check {
    final private Creature parent;
    final private CreatureType childType;

    public CloneCheck(final Creature parent) {
        this.parent = parent;
        this.childType = parent.getType();
    }

    @Override
    public Optional<Creature> checkPartyToAct() {
        if (parent.isReadyToReproduce(false)) {
            double probability = Config.CONFIG.reproduceProbability(childType);
            if (ThreadLocalRandom.current().nextDouble() < probability) {
                return Optional.of(parent);
            } else {
                Logger.Log(LogSources.CREATURE, LogLevels.DEBUG,
                        String.format("probability failed for %s", parent));
            }
        }
        return Optional.empty();
    }

}
