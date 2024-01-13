package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.reproduce.BornAction;

public abstract class Animal extends Creature {
    public Animal(CreatureType type, int health) {
        super(type, health);
    }

    @Override
    public void tryToReproduce() {
        final BornAction bornAction = new BornAction(this);
        if (bornAction.checkReady()) {
            bornAction.doAction();
        }
    }
}
