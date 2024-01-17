package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.reproduce.CloneAction;
import org.yt.jr.projects.creatures.actions.reproduce.CloneCheck;

public class Plant extends Creature {

    public Plant(int health) {
        super(CreatureType.PLANT, health);
        reproduceCheck = CloneCheck::new;
        reproduceAction = CloneAction::new;
    }

    @Override
    protected void tryToReproduce() {
        if (reproduceCheck.apply(this).checkReadyToAct().isPresent()) {
            reproduceAction.apply(this).doAction(this);
        }
    }
}
