package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.CreatureType;

import java.util.ArrayList;
import java.util.List;

public enum LifeCycleType {
    PLANTS,
    HERBIVORES,
    CARNIVORES;

    public List<CreatureType> getTypes() {
        final List<CreatureType> typesList = new ArrayList<>();

        for (CreatureType creatureType : CreatureType.values()) {
            if (this.equals(creatureType.getLifeCycleType())) {
                typesList.add(creatureType);
            }
        }
        return typesList;
    }
}
