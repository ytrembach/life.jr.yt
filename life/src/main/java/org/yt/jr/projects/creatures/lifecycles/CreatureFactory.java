package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.Plant;
import org.yt.jr.projects.utils.CreaturesTypes;

import java.util.Map;
import java.util.function.Function;

public class CreatureFactory {
    final public static CreatureFactory CREATURE_FACTORY = new CreatureFactory();

    final private Map<CreaturesTypes, Function<Integer,Creature>> generators = Map.of(
            CreaturesTypes.PLANTS, Plant::new);

    public Function<Integer,Creature> getCreature(CreaturesTypes type) {
        return generators.get(type);
    }
}
