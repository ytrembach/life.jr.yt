package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.creatures.actions.eat.EatAction;
import org.yt.jr.projects.creatures.actions.eat.EatCheck;
import org.yt.jr.projects.creatures.actions.reproduce.BornAction;
import org.yt.jr.projects.creatures.actions.reproduce.BornCheck;

import java.util.Optional;
import java.util.function.Function;

public abstract class Animal extends Creature {
    final private Function<Creature, Check> eatCheck;
    final private Function<Creature, Action> eatAction;

    public Animal(CreatureType type, int health) {
        super(type, health);
        reproduceCheck = BornCheck::new;
        reproduceAction = BornAction::new;

        eatCheck = EatCheck::new;
        eatAction = EatAction::new;
    }

    @Override
    protected void tryToReproduce() {
        Optional<Creature> secondParentFound = reproduceCheck.apply(this).checkPartyToAct();
        secondParentFound.ifPresent(secondParent -> reproduceAction.apply(this).doAction(secondParent));
    }

    @Override
    public void run() {
        super.run();

        // eat
        Optional<Creature> foodFound;
        while ((foodFound = eatCheck.apply(this).checkPartyToAct()).isPresent()) {
            foodFound.ifPresent(food -> eatAction.apply(this).doAction(food));
        }
    }
}
