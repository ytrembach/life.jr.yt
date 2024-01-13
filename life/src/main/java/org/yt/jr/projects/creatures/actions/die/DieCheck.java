package org.yt.jr.projects.creatures.actions.die;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.utils.logs.Config;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class DieCheck implements Check {

    final private Creature creature;

    public DieCheck(final Creature creature) {
        this.creature = creature;
    }

    @Override
    public Optional<Creature> checkPartyToAct() {
        final double maxAge = Config.CONFIG.maxAge(creature.getType());
        final double deathProbability = -1 / Math.exp(1) * Math.log((maxAge - creature.getAge()) / maxAge);
        final double random = ThreadLocalRandom.current().nextDouble();

        if (creature.getHealth() == 0 || creature.getAge() >= maxAge || random < deathProbability) {
            return Optional.of(creature);
        } else {
            return Optional.empty();
        }
    }

}
