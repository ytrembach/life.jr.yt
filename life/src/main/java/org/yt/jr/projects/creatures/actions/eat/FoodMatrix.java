package org.yt.jr.projects.creatures.actions.eat;

import org.yt.jr.projects.creatures.CreatureType;
import org.yt.jr.projects.utils.Config;

import java.util.HashMap;
import java.util.Map;

public class FoodMatrix {

    final public static FoodMatrix FOOD_MATRIX = new FoodMatrix();

    final private Map<CreatureType, Map<CreatureType, Float>> foodMatrix = new HashMap<>();

    public void initFoodMatrix() {
        for (CreatureType eaterType : CreatureType.values()) {
            for (CreatureType foodType : CreatureType.values()) {
                setProbability(eaterType, foodType, Config.getConfig().foodMatrixValue(eaterType, foodType));
            }
        }
    }

    public float getProbability(final CreatureType eaterType, final CreatureType foodType) {
        Map<CreatureType, Float> eaterMap = foodMatrix.get(eaterType);
        float probability = 0;
        if (eaterMap != null) {
            probability = eaterMap.get(foodType);
        }
        return probability;
    }

    public void setProbability(final CreatureType eaterType, final CreatureType foodType, final float probability) {
        Map<CreatureType, Float> eaterMap = foodMatrix.computeIfAbsent(eaterType, k -> new HashMap<>());
        eaterMap.put(foodType, probability);
    }
}
