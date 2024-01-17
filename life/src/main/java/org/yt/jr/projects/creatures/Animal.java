package org.yt.jr.projects.creatures;

import org.yt.jr.projects.creatures.actions.Action;
import org.yt.jr.projects.creatures.actions.Check;
import org.yt.jr.projects.creatures.actions.eat.EatAction;
import org.yt.jr.projects.creatures.actions.eat.EatCheck;
import org.yt.jr.projects.creatures.actions.move.MoveAction;
import org.yt.jr.projects.creatures.actions.move.MoveCheck;
import org.yt.jr.projects.creatures.actions.reproduce.BornAction;
import org.yt.jr.projects.creatures.actions.reproduce.BornCheck;
import org.yt.jr.projects.utils.Config;
import org.yt.jr.projects.utils.logs.LogLevels;
import org.yt.jr.projects.utils.logs.LogSources;
import org.yt.jr.projects.utils.logs.Logger;

import java.util.Optional;
import java.util.function.Function;

public abstract class Animal extends Creature {
    final private Function<Creature, Check> eatCheck;
    final private Function<Creature, Action> eatAction;
    final private Function<Creature, Check> moveCheck;
    final private Function<Creature, Action> moveAction;

    public Animal(CreatureType type, int health) {
        super(type, health);
        reproduceCheck = BornCheck::new;
        reproduceAction = BornAction::new;

        eatCheck = EatCheck::new;
        eatAction = EatAction::new;

        moveCheck = MoveCheck::new;
        moveAction = MoveAction::new;
    }

    @Override
    protected void tryToReproduce() {
        Optional<Creature> secondParentFound = reproduceCheck.apply(this).checkReadyToAct();
        secondParentFound.ifPresent(secondParent -> reproduceAction.apply(this).doAction(secondParent));
    }

    @Override
    public void run() {
        super.run();

        try {
            if (isAlive()) {
                // eat
                Optional<Creature> foodFound;
                for (int i = 0; i < Config.getConfig().eatAttemptsPerMove(type); i++) {
                    foodFound = eatCheck.apply(this).checkReadyToAct();
                    foodFound.ifPresent(food -> eatAction.apply(this).doAction(food));
                }

                // move
                if (moveCheck.apply(this).checkReadyToAct().isPresent()) {
                    moveAction.apply(this).doAction(this);
                }
            }
        } catch (Exception e) {
            Logger.Log(LogSources.CREATURE, LogLevels.ERROR, String.format("Exception %s, message: %s", e, e.getMessage()));
            for (StackTraceElement el : e.getStackTrace()) {
                Logger.Log(LogSources.CREATURE, LogLevels.ERROR, el.toString());
            }
        }
    }
}
