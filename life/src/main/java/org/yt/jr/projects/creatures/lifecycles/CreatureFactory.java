package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.Plant;
import org.yt.jr.projects.creatures.animals.Deer;
import org.yt.jr.projects.creatures.animals.Horse;
import org.yt.jr.projects.creatures.CreatureType;

import java.util.Map;
import java.util.function.Function;

public class CreatureFactory {
    final public static CreatureFactory CREATURE_FACTORY = new CreatureFactory();

    final private Map<CreatureType, Function<Integer,Creature>> generators = Map.of(
            CreatureType.PLANT, Plant::new,

            CreatureType.HORSE, Horse::new,
            CreatureType.DEER, Deer::new
    );

    public Function<Integer,Creature> getCreature(CreatureType type) {
        return generators.get(type);
    }
}
