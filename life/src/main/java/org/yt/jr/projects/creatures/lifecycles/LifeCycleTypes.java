package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.CreaturesTypes;

import java.util.ArrayList;
import java.util.List;

public enum LifeCycleTypes {
    EMPTY,
    PLANTS,
    HERBIVORES,
    CARNIVORES;

    public List<CreaturesTypes> getTypes() {
        final List<CreaturesTypes> typesList = new ArrayList<>();

        for (CreaturesTypes creaturesType : CreaturesTypes.values()) {
            if (this.equals(creaturesType.getLifeCycleType())) {
                typesList.add(creaturesType);
            }
        }
        return typesList;
    }
}
