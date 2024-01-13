package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.reproduce.BornAction;
import org.yt.jr.projects.creatures.actions.reproduce.BornCheck;

import java.util.Optional;

public abstract class Animal extends Creature {

    public Animal(CreatureType type, int health) {
        super(type, health);
        reproduceCheck = BornCheck::new;
        reproduceAction = BornAction::new;
    }

    @Override
    protected void tryToReproduce() {
        Optional<Creature> partyFound =reproduceCheck.apply(this).checkPartyToAct();
        if (partyFound.isPresent()) {
            reproduceAction.apply(this).doAction(partyFound.get());
        }
    }
}
