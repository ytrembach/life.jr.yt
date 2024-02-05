package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.lifecycles.LifeCycleType;

public enum CreatureType {
    PLANT(LifeCycleType.PLANTS),

    HORSE(LifeCycleType.HERBIVORES),
    DEER(LifeCycleType.HERBIVORES),
    RABBIT(LifeCycleType.HERBIVORES),
    MOUSE(LifeCycleType.HERBIVORES),
    GOAT(LifeCycleType.HERBIVORES),
    SHEEP(LifeCycleType.HERBIVORES),
    BOAR(LifeCycleType.HERBIVORES),
    BUFFALO(LifeCycleType.HERBIVORES),
    DUCK(LifeCycleType.HERBIVORES),
    CATERPILLAR(LifeCycleType.HERBIVORES),

    WOLF(LifeCycleType.CARNIVORES),
    BOA(LifeCycleType.CARNIVORES),
    FOX(LifeCycleType.CARNIVORES),
    BEAR(LifeCycleType.CARNIVORES),
    EAGLE(LifeCycleType.CARNIVORES);

    final private LifeCycleType lifeCycleType;

    CreatureType(LifeCycleType lifeCycleType) {
        this.lifeCycleType = lifeCycleType;
    }

    public LifeCycleType getLifeCycleType() {
        return lifeCycleType;
    }
}
