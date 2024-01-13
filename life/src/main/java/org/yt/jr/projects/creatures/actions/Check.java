package org.yt.jr.projects.creatures.actions;

import org.yt.jr.projects.creatures.Creature;

import java.util.Optional;

@FunctionalInterface
public interface Check {
    Optional<Creature> checkPartyToAct();
}
