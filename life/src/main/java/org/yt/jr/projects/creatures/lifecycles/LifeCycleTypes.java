package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.utils.CreaturesTypes;
public enum LifeCycleTypes {
    PLANTS(CreaturesTypes.PLANTS),
    HERBIVOROUS,
    CARNIVOROUS;

    private CreaturesTypes[] types;

    LifeCycleTypes(CreaturesTypes... types) {
        this.types = types;
    }

    public CreaturesTypes[] getTypes() {
        return types;
    }
}
