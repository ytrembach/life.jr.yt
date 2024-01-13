package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.reproduce.CloneAction;

public class Plant extends Creature {
    public Plant(int health) {
        super(CreatureType.PLANT, health);
    }

    @Override
    public void tryToReproduce() {
        final CloneAction cloneAction = new CloneAction(this);
        if (cloneAction.checkReady()) {
            cloneAction.doAction();
        }
    }
}
