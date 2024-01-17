package org.yt.jr.projects.creatures.lifecycles;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.Plant;
import org.yt.jr.projects.creatures.animals.carnivores.*;
import org.yt.jr.projects.creatures.animals.herbivores.*;
import org.yt.jr.projects.creatures.CreatureType;

import java.util.*;
import java.util.function.Function;


public class CreatureFactory {
    final public static CreatureFactory CREATURE_FACTORY = new CreatureFactory();

    final private Map<CreatureType, Function<Integer, Creature>> generators = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.PLANT, Plant::new),

            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.HORSE, Horse::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.DEER, Deer::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.RABBIT, Rabbit::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.MOUSE, Mouse::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.GOAT, Goat::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.SHEEP, Sheep::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.BOAR, Boar::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.BUFFALO, Buffalo::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.DUCK, Duck::new),

            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.CATERPILLAR, Caterpillar::new),

            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.WOLF, Wolf::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.BOA, Boa::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.FOX, Fox::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.BEAR, Bear::new),
            new AbstractMap.SimpleImmutableEntry<CreatureType, Function<Integer, Creature>> (CreatureType.EAGLE, Eagle::new)
    );

    public Function<Integer, Creature> getCreature(CreatureType type) {
        return generators.get(type);
    }
}
