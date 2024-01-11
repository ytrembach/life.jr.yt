package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.lifecycles.LifeCycleTypes;

public enum CreaturesTypes {
    NONEXISTENT(LifeCycleTypes.EMPTY),
    PLANTS(LifeCycleTypes.PLANTS),
    HORSE(LifeCycleTypes.HERBIVORES),
    DEER(LifeCycleTypes.HERBIVORES);

    final private LifeCycleTypes lifeCycleType;

    CreaturesTypes(LifeCycleTypes lifeCycleType) {
        this.lifeCycleType = lifeCycleType;
    }

    public LifeCycleTypes getLifeCycleType() {
        return lifeCycleType;
    }
}
