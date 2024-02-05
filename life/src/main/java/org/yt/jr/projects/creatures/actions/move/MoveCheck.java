package org.yt.jr.projects.creatures.actions.move;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.creatures.actions.eat.FoodMatrix;
import org.yt.jr.projects.utils.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MoveCheck implements Check {

    final private Creature creature;

    public MoveCheck(Creature creature) {
        this.creature = creature;
    }

    @Override
    public Optional<Creature> checkReadyToAct() {
        final CreatureType ownType = creature.getType();
        if (Config.getConfig().maxStepsPerMove(ownType) == 0 || creature.getLocation().getHabitants() == null) {
            return Optional.empty();
        }

        final HashSet<CreatureType> ownFoodTypes = new HashSet<>();
        final List<Creature> habitants = List.copyOf(creature.getLocation().getHabitants());

        final long totalFoodCount = habitants.stream()
                .filter(food -> FoodMatrix.FOOD_MATRIX.getProbability(ownType, food.getType()) > 0)
                .peek(food -> ownFoodTypes.add(food.getType()))
                .count();

        Stream<Creature> eaters = Stream.empty();
        for (CreatureType foodType : ownFoodTypes) {
            eaters = Stream.concat(eaters, habitants.stream()
                    .filter(eater -> FoodMatrix.FOOD_MATRIX.getProbability(eater.getType(), foodType) > 0));
        }
        final long totalEaterCount = eaters.distinct().count();

        if (totalFoodCount < totalEaterCount) {
            return Optional.of(creature);
        } else {
            return Optional.empty();
        }
    }
}