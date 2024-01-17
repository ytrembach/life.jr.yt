package org.yt.jr.projects.creatures.actions.eat;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import static org.yt.jr.projects.creatures.Creature.less;
import static org.yt.jr.projects.creatures.Creature.more;

public class EatAction implements Action {

    private final Creature eater;

    public EatAction(final Creature eater) {
        this.eater = eater;
    }

    @Override
    public void doAction(final Creature food) {
        boolean result = false;
        final String foodString = food.toString();

        synchronized (less(eater, food)) {
            synchronized (more(eater, food)) {
                if (eater.getLocation().equals(food.getLocation())) {
                    float foodWeight = Config.getConfig().ownWeight(food.getType());
                    float needFoodWeight = Config.getConfig().needFoodWeight(eater.getType());
                    float maxHealth = Config.getConfig().creatureDefaultHealthMax(eater.getType());
                    int newHealth = Math.round(Math.min(eater.getHealth() + foodWeight / needFoodWeight * maxHealth, maxHealth));
                    eater.setHealth(newHealth);
                    food.getDieAction().apply(food).doAction(food);
                    result = true;
                }
            }
        }
        if (result) {
            Logger.Log(LogSources.ACTION, LogLevels.INFO,
                    String.format("%s ate %s", eater, foodString));
        } else {
            Logger.Log(LogSources.ACTION, LogLevels.ERROR,
                    String.format("%s failed to eat %s in EatAction", eater, foodString));
        }
    }
}
