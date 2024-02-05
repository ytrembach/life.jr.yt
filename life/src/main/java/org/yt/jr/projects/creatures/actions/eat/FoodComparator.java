package org.yt.jr.projects.creatures.actions.eat;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.utils.Config;

import java.util.Comparator;

public class FoodComparator implements Comparator<Creature> {

    final private CreatureType eaterType;

    public FoodComparator(final Creature eater) {
        this.eaterType = eater.getType();
    }

    @Override
    public int compare(Creature c1, Creature c2) {
        final CreatureType c1Type = c1.getType();
        final CreatureType c2Type = c2.getType();
        return Double.compare(
                Config.getConfig().ownWeight(c1Type) * Config.getConfig().foodMatrixValue(eaterType,c1Type),
                Config.getConfig().ownWeight(c2Type) * Config.getConfig().foodMatrixValue(eaterType,c2Type)
        );
    }
}
