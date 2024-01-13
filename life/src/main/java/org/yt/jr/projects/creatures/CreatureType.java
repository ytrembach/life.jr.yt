package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.lifecycles.LifeCycleType;

public enum CreatureType {
    NONEXISTENT(LifeCycleType.EMPTY),
    PLANT(LifeCycleType.PLANTS),
    HORSE(LifeCycleType.HERBIVORES),
    DEER(LifeCycleType.HERBIVORES);

    final private LifeCycleType lifeCycleType;

    CreatureType(LifeCycleType lifeCycleType) {
        this.lifeCycleType = lifeCycleType;
    }

    public LifeCycleType getLifeCycleType() {
        return lifeCycleType;
    }
}
