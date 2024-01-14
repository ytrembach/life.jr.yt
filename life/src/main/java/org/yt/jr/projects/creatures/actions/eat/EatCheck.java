package org.yt.jr.projects.creatures.actions.eat;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.utils.Config;

import java.util.Optional;

public class EatCheck implements Check {

    final private Creature creature;

    public EatCheck(final Creature creature) {
        this.creature = creature;
    }

    @Override
    public Optional<Creature> checkPartyToAct() {
        if (creature.getHealth() < Config.CONFIG.creatureDefaultHealth("max")) {
            Optional<Creature> food = selectVictimToEat(creature);
            return food;
        }
        return Optional.empty();
    }

    public static Optional<Creature> selectVictimToEat(final Creature eater) {
        final CreatureType eaterType = eater.getType();
        return eater.getLocation().getHabitants().stream()
                .filter(foodCandidate -> foodCandidate != eater)
                .filter(foodCandidate -> FoodMatrix.FOOD_MATRIX.getProbability(eaterType, foodCandidate.getType()) > 0)
                .findAny();
    }
}
