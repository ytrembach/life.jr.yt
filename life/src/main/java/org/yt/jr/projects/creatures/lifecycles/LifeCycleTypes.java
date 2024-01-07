package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.utils.CreaturesTypes;
public enum LifeCycleTypes {
    PLANTS(CreaturesTypes.PLANTS),
    HERBIVORES(
            CreaturesTypes.HORSE,
            CreaturesTypes.DEER),
    PREDATORS;

    final private CreaturesTypes[] types;

    LifeCycleTypes(CreaturesTypes... types) {
        this.types = types;
    }

    public CreaturesTypes[] getTypes() {
        return types;
    }
}
