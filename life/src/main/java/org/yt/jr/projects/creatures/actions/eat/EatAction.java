package org.yt.jr.projects.creatures.actions.eat;

import org.yt.jr.projects.creatures.Creature;
import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.maps.Location;
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
        synchronized (less(eater, food)) {
            synchronized (more(eater, food)) {
                Logger.Log(LogSources.ACTION, LogLevels.DEBUG, String.format("%s ate %s", eater, food));
                float foodWeight = Config.CONFIG.ownWeight(food.getType());
                float needFoodWeight = Config.CONFIG.needFoodWeight(eater.getType());
                float maxHealth = Config.CONFIG.creatureDefaultHealth("max");
                int newHealth = Math.round(Math.min(eater.getHealth() + foodWeight / needFoodWeight * maxHealth, maxHealth));
                eater.setHealth(newHealth);
                food.getDieAction().apply(food).doAction(food);
            }
        }
    }
}
